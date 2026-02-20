package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Subject Controller", description = "API for managing subjects")
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

	private final SubjectService subjectService;

	@Operation(summary = "Create new subject")
	@PostMapping
	@PreAuthorize("hasAuthority('SUBJECT_CREATE')")
	public ResponseEntity<BasicResponse> createSubject(@RequestBody SubjectCreateRequestDTO request) {

		SubjectGetResponseDTO response = subjectService.createSubject(request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_ADDED, response));
	}

	@Operation(summary = "Update existing subject")
	@PutMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
	public ResponseEntity<BasicResponse> updateSubject(@PathVariable Long subjectId,
			@RequestBody SubjectUpdateRequestDTO request) {

		SubjectGetResponseDTO response = subjectService.updateSubject(subjectId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_UPDATED, response));
	}

	@Operation(summary = "Get subject by ID")
	@GetMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectById(@PathVariable Long subjectId) {

		SubjectGetResponseDTO response = subjectService.getSubjectById(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get all subjects")
	@GetMapping
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getAllSubjects(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getAllSubjects(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Get subjects by department ID")
	@GetMapping("/by-department/{departmentId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectsByDepartmentId(@PathVariable Long departmentId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getSubjectsByDepartmentId(departmentId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Get subjects by college ID")
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> getSubjectsByCollegeId(@PathVariable Long collegeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.getSubjectsByCollegeId(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Search subjects by name or code")
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('SUBJECT_READ')")
	public ResponseEntity<BasicResponse> searchSubjects(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<SubjectGetResponseDTO> subjects = subjectService.searchSubjects(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Delete subject by ID")
	@DeleteMapping("/{subjectId}")
	@PreAuthorize("hasAuthority('SUBJECT_DELETE')")
	public ResponseEntity<BasicResponse> deleteSubject(@PathVariable Long subjectId) {

		subjectService.deleteSubject(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_DELETED));
	}
}
