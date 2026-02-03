package com.exam.exam_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.College;
import com.exam.exam_system.Entities.Department;
import com.exam.exam_system.Entities.Subject;
import com.exam.exam_system.dto.SubjectCreateRequestDTO;
import com.exam.exam_system.dto.SubjectGetResponseDTO;
import com.exam.exam_system.dto.SubjectUpdateRequestDTO;
import com.exam.exam_system.exception.CollegeNotFoundException;
import com.exam.exam_system.exception.DepartmentCollegeMismatchException;
import com.exam.exam_system.exception.DepartmentNotFoundException;
import com.exam.exam_system.exception.SubjectAlreadyExistsException;
import com.exam.exam_system.exception.SubjectCodeAlreadyExistsException;
import com.exam.exam_system.exception.SubjectDeletionNotAllowedException;
import com.exam.exam_system.exception.SubjectNotFoundException;
import com.exam.exam_system.mapper.SubjectMapper;
import com.exam.exam_system.repository.CollegeRepository;
import com.exam.exam_system.repository.DepartmentRepository;
import com.exam.exam_system.repository.ExamRepository;
import com.exam.exam_system.repository.SubjectRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class SubjectService {

	private final SubjectRepository subjectRepository;
	private final SubjectMapper subjectMapper;
	private final DepartmentRepository departmentRepository;
	private final CollegeRepository collegeRepository;
	private final ExamRepository examRepository;

	@Transactional
	public SubjectGetResponseDTO createSubject(@Valid SubjectCreateRequestDTO dto) {
		if (subjectRepository.existsBySubjectNameAndDepartment_DepartmentId(dto.getSubjectName(),
				dto.getDepartmentId())) {
			throw new SubjectAlreadyExistsException();
		}

		if (subjectRepository.existsBySubjectCode(dto.getSubjectCode())) {
			throw new SubjectCodeAlreadyExistsException();
		}
		Department department = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);
		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}

		Subject subject = subjectMapper.toEntity(dto);
		subject.setDepartment(department);
		subject.setCollege(college);
		Subject savedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(savedSubject);
	}

	@Transactional
	public SubjectGetResponseDTO updateSubject(Long subjectId, @Valid SubjectUpdateRequestDTO dto) {
		Subject subject = subjectRepository.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

		if (subjectRepository.existsBySubjectNameAndDepartment_DepartmentIdAndSubjectIdNot(dto.getSubjectName(),
				dto.getDepartmentId(), subjectId)) {
			throw new SubjectAlreadyExistsException();
		}

		if (subjectRepository.existsBySubjectCodeAndSubjectIdNot(dto.getSubjectCode(), subjectId)) {
			throw new SubjectCodeAlreadyExistsException();
		}

		Department department = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(DepartmentNotFoundException::new);
		College college = collegeRepository.findById(dto.getCollegeId()).orElseThrow(CollegeNotFoundException::new);

		if (!department.getCollege().getCollegeId().equals(college.getCollegeId())) {
			throw new DepartmentCollegeMismatchException();
		}

		subject.setSubjectName(dto.getSubjectName());
		subject.setSubjectCode(dto.getSubjectCode());
		subject.setDepartment(department);
		subject.setCollege(college);

		Subject updatedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(updatedSubject);
	}

	public SubjectGetResponseDTO getSubjectById(Long subjectId) {

		Subject subject = subjectRepository.findByIdWithRelations(subjectId).orElseThrow(SubjectNotFoundException::new);
		return subjectMapper.toDto(subject);
	}

	public Page<SubjectGetResponseDTO> getAllSubjects(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("subjectName"));

		Page<Subject> subjects = subjectRepository.findAllWithDepartmentAndCollege(pageable);

		return subjects.map(subjectMapper::toDto);
	}

	public Page<SubjectGetResponseDTO> getSubjectsByDepartmentId(Long departmentId, int page, int size) {

		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("subjectName"));

		return subjectRepository.findAllByDepartmentIdWithRelations(departmentId, pageable).map(subjectMapper::toDto);
	}

	public Page<SubjectGetResponseDTO> getSubjectsByCollegeId(Long collegeId, int page, int size) {

		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("subjectName"));

		return subjectRepository.findAllByCollegeIdWithRelations(collegeId, pageable).map(subjectMapper::toDto);
	}

	public Page<SubjectGetResponseDTO> searchSubjects(String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("subjectName"));

		return subjectRepository.searchByNameOrCodeWithRelations(keyword, pageable).map(subjectMapper::toDto);
	}

	@Transactional
	public void deleteSubject(Long subjectId) {
		if (!subjectRepository.existsById(subjectId)) {
			throw new SubjectNotFoundException();
		}
		if (examRepository.existsBySubject_SubjectId(subjectId)) {
			throw new SubjectDeletionNotAllowedException();
		}

		subjectRepository.deleteById(subjectId);
	}
}
