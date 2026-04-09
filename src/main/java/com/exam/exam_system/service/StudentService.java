package com.exam.exam_system.service;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.dto.*;
import com.exam.exam_system.entities.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.StudentMapper;
import com.exam.exam_system.repository.CollegeRepository;
import com.exam.exam_system.repository.DepartmentRepository;
import com.exam.exam_system.repository.RoleRepository;
import com.exam.exam_system.repository.StudentRepository;
import com.exam.exam_system.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class StudentService extends BaseService {

	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final StudentMapper studentMapper;
	private final RoleRepository roleRepository;
	private final CollegeRepository collegeRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;

	private final UserService userService;

	@Transactional
	public StudentProfileResponseDTO registerStudentAndReturnProfile(@Valid StudentRegisterRequestDTO dto) {

		// 1️⃣ Check username/email/phone
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}

		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new EmailAlreadyExistsException();
		}

		if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
			throw new PhoneAlreadyExistsException();
		}

		// 2️⃣ Check studentCode
		if (studentRepository.existsByStudentCode(dto.getStudentCode())) {
			throw new StudentCodeAlreadyExistsException();
		}

		// 3️⃣ Get role STUDENT automatically
		Role studentRole = roleRepository.findByRoleName("STUDENT").orElseThrow(RoleNotFoundException::new);

		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		Department department = departmentRepository.findByIdWithCollege(dto.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}

		// 4️⃣ Create User
		User user = User.builder().username(dto.getUsername()).email(dto.getEmail())
				.password(passwordEncoder.encode(dto.getPassword())).firstName(dto.getFirstName())
				.lastName(dto.getLastName()).phone(dto.getPhone()).role(studentRole).college(college)
				.department(department).isActive(true).build();

		User savedUser = userRepository.save(user);

		// 5️⃣ Create Student
		Student student = Student.builder().user(savedUser).studentCode(dto.getStudentCode())
				.academicYear(dto.getAcademicYear()).isActive(true).build();

		Student savedStudent = studentRepository.save(student);

		return studentMapper.toProfileDto(savedStudent);
	}

	@Transactional(readOnly = true)
	public StudentProfileResponseDTO getMyProfile() {

		User user = userService.getCurrentUser();
		Student student = studentRepository.findByUserIdWithFullProfile(user.getUserId())
				.orElseThrow(StudentNotFoundException::new);
		return studentMapper.toProfileDto(student);
	}

	@Transactional
	@CacheEvict(value = "students", key = "#studentId")
	public StudentGetResponseDTO updateStudent(Long studentId, @Valid StudentUpdateRequestDTO dto) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		if (studentRepository.existsByStudentCodeAndStudentIdNot(dto.getStudentCode(), studentId)) {
			throw new StudentCodeAlreadyExistsException();
		}
		studentMapper.updateStudentFromDto(dto, student);

		return studentMapper.toDto(studentRepository.save(student));
	}

	@Transactional
	public StudentProfileResponseDTO updateMyProfile(@Valid StudentUpdateProfileRequestDTO dto) {

		User user = userService.getCurrentUser();
		Student student = studentRepository.findByUser_UserId(user.getUserId())
				.orElseThrow(StudentNotFoundException::new);

		if (studentRepository.existsByStudentCodeAndStudentIdNot(dto.getStudentCode(), student.getStudentId())) {
			throw new StudentCodeAlreadyExistsException();
		}

		studentMapper.updateStudentProfileFromDto(dto, student);

		Student saved = studentRepository.save(student);

		// Evict this specific student from cache after resolving studentId
		evictStudentCache(saved.getStudentId());

		return studentMapper.toProfileDto(saved);
	}

	@CacheEvict(value = "students", key = "#studentId")
	public void evictStudentCache(Long studentId) {
		// intentionally empty — annotation handles eviction
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "students", key = "#p0")
	public StudentGetResponseDTO getStudentById(Long studentId) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		return studentMapper.toDto(student);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getAllStudents(int page, int size) {

		Pageable pageable = createPageRequest(page, size, "studentCode");

		return studentRepository.findAllWithUser(pageable).map(studentMapper::toDto);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getAllActiveStudents(int page, int size) {

		Pageable pageable = createPageRequest(page, size, "studentCode");

		return studentRepository.findByIsActiveTrueWithUser(pageable).map(studentMapper::toDto);
	}

	@Transactional
	@CacheEvict(value = "students", key = "#studentId")
	public void deleteStudent(Long studentId) {

		if (!studentRepository.existsById(studentId)) {
			throw new StudentNotFoundException();
		}

		studentRepository.deleteById(studentId);
	}

	@Transactional
	@CacheEvict(value = "students", key = "#studentId")
	public void deactivateStudent(Long studentId) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		if (!Boolean.TRUE.equals(student.getIsActive())) {
			throw new StudentAlreadyDeactivatedException();
		}

		student.setIsActive(false);
		student.setDeactivatedAt(LocalDateTime.now());

		student.getUser().setIsActive(false);
	}

	@Transactional
	@CacheEvict(value = "students", key = "#studentId")
	public void activateStudent(Long studentId) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		if(Boolean.TRUE.equals(student.getIsActive())) {
			throw new StudentAlreadyActiveException();
		}
		student.setIsActive(true);
		student.setDeactivatedAt(null);
		student.getUser().setIsActive(true);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> searchStudents(String studentCode, Integer academicYear, Boolean isActive,
			int page, int size) {

		String code = (studentCode == null) ? null : studentCode.trim().toLowerCase();

		Pageable pageable = createPageRequest(page, size, "studentCode");

		return studentRepository.searchStudents(code, academicYear, isActive, pageable).map(studentMapper::toDto);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "studentStats", key = "'countByYear_' + #year")
	public long countStudentsByYear(Integer year) {
		return studentRepository.countByAcademicYear(year);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getStudentsByDepartment(Long departmentId, int page, int size) {
		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}
		Pageable pageable = createPageRequest(page, size, "studentCode");
		return studentRepository.findByUser_Department_DepartmentIdWithUser(departmentId, pageable).map(studentMapper::toDto);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getStudentsByCollege(Long collegeId, int page, int size) {
		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}
		Pageable pageable = createPageRequest(page, size, "studentCode");
		return studentRepository.findByUser_College_CollegeIdWithUser(collegeId, pageable).map(studentMapper::toDto);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "studentStats", key = "'countActive'")
	public long countActiveStudents() {
		return studentRepository.countByIsActiveTrue();
	}

}