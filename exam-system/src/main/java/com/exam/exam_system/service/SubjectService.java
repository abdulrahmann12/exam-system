package com.exam.exam_system.service;

import java.util.List;

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

	public List<SubjectGetResponseDTO> getAllSubjects() {
		List<Subject> subjects = subjectRepository.findAllWithDepartmentAndCollege();
		return subjects.stream().map(subjectMapper::toDto).toList();
	}

	public List<SubjectGetResponseDTO> getSubjectsByDepartmentId(Long departmentId) {
		if (!departmentRepository.existsById(departmentId)) {
			throw new DepartmentNotFoundException();
		}
		List<Subject> subjects = subjectRepository.findAllByDepartmentIdWithRelations(departmentId);
		return subjects.stream().map(subjectMapper::toDto).toList();
	}

	public List<SubjectGetResponseDTO> getSubjectsByCollegeId(Long collegeId) {
		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}
		List<Subject> subjects = subjectRepository.findAllByCollegeIdWithRelations(collegeId);
		return subjects.stream().map(subjectMapper::toDto).toList();
	}

	public List<SubjectGetResponseDTO> searchSubjects(String keyword) {

		return subjectRepository
				.searchByNameOrCodeWithRelations(keyword).stream()
				.map(subjectMapper::toDto).toList();
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
