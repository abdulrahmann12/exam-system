package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = SwaggerMessages.TAG_SUBJECT, description = SwaggerMessages.TAG_SUBJECT_DESC)
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

	private final SubjectService subjectService;

	@Operation(summary = SwaggerMessages.CREATE_SUBJECT, description = SwaggerMessages.CREATE_SUBJECT_DESC)
	@PostMapping
	@PreAuthorize("hasAuthority('SUBJECT_CREATE')")
	public ResponseEntity<BasicResponse> createSubject(@Valid @RequestBody SubjectCreateRequestDTO request) {

		SubjectGetResponseDTO response = subjectService.createSubject(request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_ADDED, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_SUBJECT, description = SwaggerMessages.UPDATE_SUBJECT_DESC)
	@PutMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
	public ResponseEntity<BasicResponse> updateSubject(@PathVariable("subjectId") Long subjectId,
			@Valid @RequestBody SubjectUpdateRequestDTO request) {

		SubjectGetResponseDTO response = subjectService.updateSubject(subjectId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_UPDATED, response));
	}

	@Operation(summary = SwaggerMessages.GET_SUBJECT_BY_ID, description = SwaggerMessages.GET_SUBJECT_BY_ID_DESC)
	@GetMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectById(@PathVariable("subjectId") Long subjectId) {

		SubjectGetResponseDTO response = subjectService.getSubjectById(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_SUBJECTS, description = SwaggerMessages.GET_ALL_SUBJECTS_DESC)
	@GetMapping
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getAllSubjects(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getAllSubjects(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = SwaggerMessages.GET_SUBJECTS_BY_DEPARTMENT, description = SwaggerMessages.GET_SUBJECTS_BY_DEPARTMENT_DESC)
	@GetMapping("/by-department/{departmentId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectsByDepartmentId(@PathVariable("departmentId") Long departmentId,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getSubjectsByDepartmentId(departmentId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = SwaggerMessages.GET_SUBJECTS_BY_COLLEGE, description = SwaggerMessages.GET_SUBJECTS_BY_COLLEGE_DESC)
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectsByCollegeId(@PathVariable("collegeId") Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getSubjectsByCollegeId(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = SwaggerMessages.SEARCH_SUBJECTS, description = SwaggerMessages.SEARCH_SUBJECTS_DESC)
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> searchSubjects(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.searchSubjects(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = SwaggerMessages.DELETE_SUBJECT, description = SwaggerMessages.DELETE_SUBJECT_DESC)
	@DeleteMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_DELETE')")
	public ResponseEntity<BasicResponse> deleteSubject(@PathVariable("subjectId") Long subjectId) {

		subjectService.deleteSubject(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_DELETED));
	}
}