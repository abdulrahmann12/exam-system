package com.exam.exam_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.College;
import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.CollegeCreateRequestDTO;
import com.exam.exam_system.dto.CollegeGetResponseDTO;
import com.exam.exam_system.dto.CollegeUpdateRequestDTO;
import com.exam.exam_system.exception.CollegeAlreadyExistsException;
import com.exam.exam_system.exception.CollegeDeletionNotAllowedException;
import com.exam.exam_system.exception.CollegeNotFoundException;
import com.exam.exam_system.mapper.CollegeMapper;
import com.exam.exam_system.repository.CollegeRepository;
import com.exam.exam_system.repository.DepartmentRepository;
import com.exam.exam_system.repository.SubjectRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class CollegeService {

	private final CollegeRepository collegeRepository;
	private final CollegeMapper collegeMapper;
	private final SubjectRepository subjectRepository;
	private final DepartmentRepository departmentRepository;

	@Transactional
	public CollegeGetResponseDTO createCollege(@Valid CollegeCreateRequestDTO dto) {

		if (collegeRepository.existsByCollegeName(dto.getCollegeName())) {
			throw new CollegeAlreadyExistsException();
		}

		College college = collegeMapper.toEntity(dto);
		College savedCollege = collegeRepository.save(college);

		return collegeMapper.toDto(savedCollege);
	}

	@Transactional
	public CollegeGetResponseDTO updateCollege(Long collegeId, @Valid CollegeUpdateRequestDTO dto) {

		College college = collegeRepository.findById(collegeId).orElseThrow(CollegeNotFoundException::new);

		if (collegeRepository.existsByCollegeNameAndCollegeIdNot(dto.getCollegeName(), collegeId)) {
			throw new CollegeAlreadyExistsException();
		}

		college.setCollegeName(dto.getCollegeName());

		College savedCollege = collegeRepository.save(college);
		return collegeMapper.toDto(savedCollege);
	}

	public CollegeGetResponseDTO getCollegeById(Long collegeId) {

		College college = collegeRepository.findById(collegeId).orElseThrow(CollegeNotFoundException::new);

		return collegeMapper.toDto(college);
	}

	public CollegeGetResponseDTO getCollegeByName(String collegeName) {

		College college = collegeRepository.findByCollegeName(collegeName).orElseThrow(CollegeNotFoundException::new);

		return collegeMapper.toDto(college);
	}

	public Page<CollegeGetResponseDTO> searchColleges(String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("collegeName"));

		Page<College> collegesPage = collegeRepository.findByCollegeNameContainingIgnoreCase(keyword, pageable);

		return collegesPage.map(collegeMapper::toDto);
	}

	public Page<CollegeGetResponseDTO> getAllColleges(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("collegeName"));

		Page<College> collegesPage = collegeRepository.findAll(pageable);

		return collegesPage.map(collegeMapper::toDto);
	}

	@Transactional
	public void deleteCollegeById(Long collegeId) {

		if (!collegeRepository.existsById(collegeId)) {
			throw new CollegeNotFoundException();
		}

		if (subjectRepository.existsByCollege_CollegeId(collegeId)) {
			throw new CollegeDeletionNotAllowedException(Messages.CANNOT_DELETE_COLLEGE_SUBJECS);
		}

		if (departmentRepository.existsByCollege_CollegeId(collegeId)) {
			throw new CollegeDeletionNotAllowedException(Messages.CANNOT_DELETE_COLLEGE_DEPARTMENT);
		}

		collegeRepository.deleteById(collegeId);
	}
}
