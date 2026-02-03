package com.exam.exam_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.College;
import com.exam.exam_system.Entities.Department;
import com.exam.exam_system.dto.DepartmentCreateRequestDTO;
import com.exam.exam_system.dto.DepartmentGetResponseDTO;
import com.exam.exam_system.dto.DepartmentUpdateRequestDTO;
import com.exam.exam_system.exception.CollegeNotFoundException;
import com.exam.exam_system.exception.DepartmentAlreadyExistsException;
import com.exam.exam_system.exception.DepartmentDeletionNotAllowedException;
import com.exam.exam_system.exception.DepartmentNotFoundException;
import com.exam.exam_system.mapper.DepartmentMapper;
import com.exam.exam_system.repository.CollegeRepository;
import com.exam.exam_system.repository.DepartmentRepository;
import com.exam.exam_system.repository.SubjectRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final DepartmentMapper departmentMapper;
	private final SubjectRepository subjectRepository;
	private final CollegeRepository collegeRepository;

	@Transactional
	public DepartmentGetResponseDTO createDepartment(@Valid DepartmentCreateRequestDTO dto) {
		if (departmentRepository.existsByDepartmentNameAndCollege_CollegeId(dto.getDepartmentName(),
				dto.getCollegeId())) {
			throw new DepartmentAlreadyExistsException();
		}
		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		Department department = departmentMapper.toEntity(dto);
		department.setCollege(college);

		Department savedDepartment = departmentRepository.save(department);

		return departmentMapper.toDto(savedDepartment);
	}

	@Transactional
	public DepartmentGetResponseDTO updateDepartment(Long departmentId, @Valid DepartmentUpdateRequestDTO dto) {

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(DepartmentNotFoundException::new);

		if (departmentRepository.existsByDepartmentNameAndCollege_CollegeIdAndDepartmentIdNot(dto.getDepartmentName(),
				dto.getCollegeId(), departmentId)) {
			throw new DepartmentAlreadyExistsException();
		}

		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		department.setDepartmentName(dto.getDepartmentName());
		department.setCollege(college);

		Department savedDepartment = departmentRepository.save(department);
		return departmentMapper.toDto(savedDepartment);
	}

	public DepartmentGetResponseDTO getDepartmentById(Long departmentId) {

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(DepartmentNotFoundException::new);

		return departmentMapper.toDto(department);
	}

	public DepartmentGetResponseDTO getDepartmentByName(String departmentName) {

		Department department = departmentRepository.findByDepartmentName(departmentName)
				.orElseThrow(DepartmentNotFoundException::new);

		return departmentMapper.toDto(department);
	}

	public Page<DepartmentGetResponseDTO> searchDepartments(String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("departmentName"));

		Page<Department> departments = departmentRepository.findByDepartmentNameContainingIgnoreCase(keyword, pageable);

		return departments.map(departmentMapper::toDto);
	}

	public Page<DepartmentGetResponseDTO> getAllDepartments(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("departmentName"));

		Page<Department> departments = departmentRepository.findAll(pageable);

		return departments.map(departmentMapper::toDto);
	}

	public Page<DepartmentGetResponseDTO> getDepartmentsByCollegeId(Long collegeId, int page, int size) {

		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("departmentName"));

		Page<Department> departments = departmentRepository.findByCollege_CollegeId(collegeId, pageable);

		return departments.map(departmentMapper::toDto);
	}

	@Transactional
	public void deleteDepartmentById(Long departmentId) {

		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}

		if (subjectRepository.existsByDepartment_DepartmentId(departmentId)) {
			throw new DepartmentDeletionNotAllowedException();
		}

		departmentRepository.deleteById(departmentId);
	}
}
