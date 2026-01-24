package com.exam.exam_system.controller;

import java.util.List;

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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> createSubject(
			@RequestBody SubjectCreateRequestDTO request) {

		SubjectGetResponseDTO response = subjectService.createSubject(request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_ADDED, response));
	}

	@Operation(summary = "Update existing subject")
	@PutMapping("/{subjectId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> updateSubject(
			@PathVariable Long subjectId,
			@RequestBody SubjectUpdateRequestDTO request) {

		SubjectGetResponseDTO response =
				subjectService.updateSubject(subjectId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_UPDATED, response));
	}

	@Operation(summary = "Get subject by ID")
	@GetMapping("/{subjectId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getSubjectById(
			@PathVariable Long subjectId) {

		SubjectGetResponseDTO response =
				subjectService.getSubjectById(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get all subjects")
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getAllSubjects() {

		List<SubjectGetResponseDTO> subjects =
				subjectService.getAllSubjects();

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Get subjects by department ID")
	@GetMapping("/by-department/{departmentId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getSubjectsByDepartmentId(
			@PathVariable Long departmentId) {

		List<SubjectGetResponseDTO> subjects =
				subjectService.getSubjectsByDepartmentId(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Get subjects by college ID")
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getSubjectsByCollegeId(
			@PathVariable Long collegeId) {

		List<SubjectGetResponseDTO> subjects =
				subjectService.getSubjectsByCollegeId(collegeId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Search subjects by name or code")
	@GetMapping("/search")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> searchSubjects(
			@RequestParam String keyword) {

		List<SubjectGetResponseDTO> subjects =
				subjectService.searchSubjects(keyword);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, subjects));
	}

	@Operation(summary = "Delete subject by ID")
	@DeleteMapping("/{subjectId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteSubject(
			@PathVariable Long subjectId) {

		subjectService.deleteSubject(subjectId);

		return ResponseEntity.ok(new BasicResponse(Messages.SUBJECT_DELETED));
	}
}
