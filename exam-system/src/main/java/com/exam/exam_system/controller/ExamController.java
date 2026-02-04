package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.ExamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Exam Controller", description = "API for managing exams")
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

	private final ExamService examService;

	@Operation(summary = "Create new exam")
	@PostMapping
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	public ResponseEntity<BasicResponse> createExam(@RequestBody CreateExamRequestDTO request) {
		ExamResponseDTO response = examService.createExam(request);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_CREATED, response));
	}

	@Operation(summary = "Update existing exam")
	@PutMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_UPDATE')")
	public ResponseEntity<BasicResponse> updateExam(@PathVariable Long examId,
			@RequestBody UpdateExamRequestDTO request) {
		ExamResponseDTO response = examService.updateExam(examId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_UPDATED, response));
	}

	@Operation(summary = "Get exam by ID")
	@GetMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<ExamResponseDTO> getExamById(@PathVariable Long examId) {
		ExamResponseDTO response = examService.getExamById(examId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Get exams by college (paginated)")
	@GetMapping("/college/{collegeId}")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public Page<ExamResponseDTO> getByCollege(@PathVariable Long collegeId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return examService.getExamsByCollege(collegeId, page, size);
	}

	@Operation(summary = "Get exams by department (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/department/{departmentId}")
	public Page<ExamResponseDTO> getByDepartment(@PathVariable Long departmentId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return examService.getExamsByDepartment(departmentId, page, size);
	}

	@Operation(summary = "Get exams by user (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/user/{userId}")
	public Page<ExamResponseDTO> getByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return examService.getExamsByUser(userId, page, size);
	}

	@Operation(summary = "Get exams by subject (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/subject/{subjectId}")
	public Page<ExamResponseDTO> getBySubject(@PathVariable Long subjectId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return examService.getExamsBySubject(subjectId, page, size);
	}

	@Operation(summary = "Get all exams (paginated)")
	@GetMapping
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<Page<ExamResponseDTO>> getAllExams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> exams = examService.getAllExams(page, size);
		return ResponseEntity.ok(exams);
	}
	
	@Operation(summary = "Get all Active exams (paginated)")
	@GetMapping("/active")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<Page<ExamResponseDTO>> getAllActiveExams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> exams = examService.getAllActiveExams(page, size);
		return ResponseEntity.ok(exams);
	}

	@Operation(summary = "Search exams by keyword (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/search")
	public Page<ExamResponseDTO> searchExams(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return examService.searchExams(keyword, page, size);
	}

	@Operation(summary = "DeActivate exam")
	@DeleteMapping("/deactivate/{examId}")
	@PreAuthorize("hasAuthority('EXAM_DELETE')")
	public ResponseEntity<BasicResponse> deActivateExam(@PathVariable Long examId) {
		examService.deActivateExam(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_DELETED, null));
	}
	
	@Operation(summary = "Delete exam")
	@DeleteMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_DELETE')")
	public ResponseEntity<BasicResponse> delateExam(@PathVariable Long examId) {
		examService.deleteExam(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_DELETED, null));
	}
}
