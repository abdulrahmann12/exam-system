package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.ExamService;
import com.exam.exam_system.service.ExamUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Exam Controller", description = "API for managing exams")
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

	private final ExamService examService;
	private final ExamUploadService examUploadService;
	private final ObjectMapper objectMapper;

	@Operation(summary = SwaggerMessages.CREATE_EXAM)
	@PostMapping
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	public ResponseEntity<BasicResponse> createExam(@Valid @RequestBody CreateExamRequestDTO request) {
		ExamResponseDTO response = examService.createExam(request);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_CREATED, response));
	}

	@Operation(summary = SwaggerMessages.CREATE_EXAM_WITH_FILE)
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	public ResponseEntity<BasicResponse> createExamWithFile(
			@RequestPart("exam") String examJson,
			@RequestPart("file") MultipartFile file) throws Exception {
		CreateExamWithFileRequestDTO examRequest = objectMapper.readValue(examJson, CreateExamWithFileRequestDTO.class);
		ExamResponseDTO response = examUploadService.createExamWithFile(examRequest, file);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_CREATED, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_EXAM)
	@PutMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_UPDATE')")
	public ResponseEntity<BasicResponse> updateExam(@PathVariable("examId") Long examId,
			@Valid @RequestBody UpdateExamRequestDTO request) {
		ExamResponseDTO response = examService.updateExam(examId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_UPDATED, response));
	}

	@Operation(summary = SwaggerMessages.GET_EXAM_BY_ID)
	@GetMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getExamById(@PathVariable("examId") Long examId) {
		ExamFullAdminViewDTO response = examService.getExamById(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_EXAMS_BY_COLLEGE)
	@GetMapping("/college/{collegeId}")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getByCollege(@PathVariable("collegeId") Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByCollege(collegeId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.GET_EXAMS_BY_DEPARTMENT)
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/department/{departmentId}")
	public ResponseEntity<BasicResponse> getByDepartment(@PathVariable("departmentId") Long departmentId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByDepartment(departmentId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_EXAMS_BY_USER)
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<BasicResponse> getByUser(@PathVariable("userId") Long userId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsByUser(userId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.GET_MY_EXAMS)
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/my")
	public ResponseEntity<BasicResponse> getMyExams(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<ExamResponseDTO> response = examService.getMyExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_EXAMS_BY_SUBJECT)
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/subject/{subjectId}")
	public ResponseEntity<BasicResponse> getBySubject(@PathVariable("subjectId") Long subjectId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getExamsBySubject(subjectId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.GET_ALL_EXAMS)
	@GetMapping
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getAllExams(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getAllExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.GET_ALL_ACTIVE_EXAMS)
	@GetMapping("/active")
	@PreAuthorize("hasAuthority('EXAM_READ')")
	public ResponseEntity<BasicResponse> getAllActiveExams(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.getAllActiveExams(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.SEARCH_EXAMS)
	@PreAuthorize("hasAuthority('EXAM_READ')")
	@GetMapping("/search")
	public ResponseEntity<BasicResponse> searchExams(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<ExamResponseDTO> response = examService.searchExams(keyword, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));

	}

	@Operation(summary = SwaggerMessages.DEACTIVATE_EXAM)
	@DeleteMapping("/deactivate/{examId}")
	@PreAuthorize("hasAuthority('EXAM_DELETE')")
	public ResponseEntity<BasicResponse> deActivateExam(@PathVariable("examId") Long examId) {
		examService.deActivateExam(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_DELETED, null));
	}

	@Operation(summary = SwaggerMessages.DELETE_EXAM)
	@DeleteMapping("/{examId}")
	@PreAuthorize("hasAuthority('EXAM_DELETE')")
	public ResponseEntity<BasicResponse> delateExam(@PathVariable("examId") Long examId) {
		examService.deleteExam(examId);
		return ResponseEntity.ok(new BasicResponse(Messages.EXAM_DELETED, null));
	}

	@PostMapping("/{examId}/qr")
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	public ResponseEntity<BasicResponse> generateQr(@PathVariable("examId") Long examId) {

		ExamQrResponseDTO response = examService.generateQrForExam(examId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.UPLOAD_EXAM_QUESTIONS)
	@PostMapping(value = "/{examId}/upload-questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('EXAM_CREATE')")
	@Deprecated(since = "2026-06", forRemoval = true)
	public ResponseEntity<BasicResponse> uploadQuestions(@PathVariable("examId") Long examId,
			@RequestParam("file") MultipartFile file) {
		UploadQuestionsResponseDTO response = examUploadService.uploadAndParseQuestions(examId, file);
		return ResponseEntity.ok(new BasicResponse(response.getMessage(), response));
	}

}