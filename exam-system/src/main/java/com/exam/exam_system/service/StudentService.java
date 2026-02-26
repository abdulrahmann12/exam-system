package com.exam.exam_system.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.Student;
import com.exam.exam_system.Entities.User;
import com.exam.exam_system.dto.StudentCreateRequestDTO;
import com.exam.exam_system.dto.StudentGetResponseDTO;
import com.exam.exam_system.dto.StudentUpdateRequestDTO;
import com.exam.exam_system.exception.StudentAlreadyDeactivatedException;
import com.exam.exam_system.exception.StudentAlreadyExistsException;
import com.exam.exam_system.exception.StudentCodeAlreadyExistsException;
import com.exam.exam_system.exception.StudentNotFoundException;
import com.exam.exam_system.exception.UserNotFoundException;
import com.exam.exam_system.mapper.StudentMapper;
import com.exam.exam_system.repository.StudentRepository;
import com.exam.exam_system.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class StudentService {

	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final StudentMapper studentMapper;

	@Transactional
	public StudentGetResponseDTO createStudent(@Valid StudentCreateRequestDTO dto) {

		if (studentRepository.existsByStudentCode(dto.getStudentCode())) {
			throw new StudentCodeAlreadyExistsException();
		}

		User user = userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new);

		if (studentRepository.existsById(user.getUserId())) {
			throw new StudentAlreadyExistsException();
		}

		Student student = studentMapper.toEntity(dto);
		student.setUser(user);

		Student savedStudent = studentRepository.save(student);

		return studentMapper.toDto(savedStudent);
	}

	@Transactional
	public StudentGetResponseDTO updateStudent(Long studentId, @Valid StudentUpdateRequestDTO dto) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		if (studentRepository.existsByStudentCodeAndStudentIdNot(dto.getStudentCode(), studentId)) {
			throw new StudentCodeAlreadyExistsException();
		}
		studentMapper.updateStudentFromDto(dto, student);

		return studentMapper.toDto(studentRepository.save(student));
	}

	public StudentGetResponseDTO getStudentById(Long studentId) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		return studentMapper.toDto(student);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getAllStudents(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("studentCode"));

		return studentRepository.findAll(pageable).map(studentMapper::toDto);
	}

	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> getAllActiveStudents(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("studentCode"));

		return studentRepository.findByIsActiveTrue(pageable).map(studentMapper::toDto);
	}

	@Transactional
	public void deleteStudent(Long studentId) {

		if (!studentRepository.existsById(studentId)) {
			throw new StudentNotFoundException();
		}

		studentRepository.deleteById(studentId);
	}

	@Transactional
	public void deactivateStudent(Long studentId) {

		Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

		if (!Boolean.TRUE.equals(student.getIsActive())) {
			throw new StudentAlreadyDeactivatedException();
		}

		student.setIsActive(false);
		student.getUser().setIsActive(false);
		student.setDeactivatedAt(LocalDateTime.now());

		studentRepository.save(student);
	}
	
	@Transactional(readOnly = true)
	public Page<StudentGetResponseDTO> searchStudents(
	        String studentCode,
	        Integer academicYear,
	        Boolean isActive,
	        int page,
	        int size
	) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("studentCode"));

	    return studentRepository.searchStudents(studentCode, academicYear, isActive, pageable)
	            .map(studentMapper::toDto);
	}
}