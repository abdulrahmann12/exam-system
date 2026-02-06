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
	public ResponseEntity<BasicResponse> getExamById(@PathVariable Long examId) {
		ExamFullAdminViewDTO response = examService.getExamById(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get exams by college (paginated)")
	@GetMapping("/college/{collegeId}")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getByCollege(@PathVariable Long collegeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByCollege(collegeId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = "Get exams by department (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/department/{departmentId}")
	public ResponseEntity<BasicResponse> getByDepartment(@PathVariable Long departmentId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByDepartment(departmentId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get exams by user (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<BasicResponse> getByUser(@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByUser(userId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = "Get my exams (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/my")
	public ResponseEntity<BasicResponse> getMyExams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<ExamResponseDTO> response = examService.getMyExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get exams by subject (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/subject/{subjectId}")
	public ResponseEntity<BasicResponse> getBySubject(@PathVariable Long subjectId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsBySubject(subjectId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = "Get all exams (paginated)")
	@GetMapping
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getAllExams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getAllExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = "Get all Active exams (paginated)")
	@GetMapping("/active")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getAllActiveExams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getAllActiveExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = "Search exams by keyword (paginated)")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/search")
	public ResponseEntity<BasicResponse> searchExams(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.searchExams(keyword, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

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

	@PostMapping("/{examId}/qr")
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	public ResponseEntity<BasicResponse> generateQr(@PathVariable Long examId) {

		ExamQrResponseDTO response = examService.generateQrForExam(examId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

}
