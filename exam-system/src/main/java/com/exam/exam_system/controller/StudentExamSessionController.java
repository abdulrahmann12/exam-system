package com.exam.exam_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.StudentExamSessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Student Exam Session Controller", description = "API for student exam sessions (QR entry, sessions, answers)")
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudentExamSessionController {

    private final StudentExamSessionService sessionService;

    @Operation(summary = "Validate QR token and get exam details")
    @GetMapping("/enter")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> enterByToken(@RequestParam String token) {
        ExamResponseDTO response = sessionService.validateTokenAndGetExam(token);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
    }

    @Operation(summary = "Start an exam session")
    @PostMapping("/start")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> startSession(@RequestParam Long examId) {
        StudentExamSessionResponseDTO response = sessionService.startSession(examId);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
    }

    @Operation(summary = "Save or update a student's answer")
    @PostMapping("/{sessionId}/answer")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> saveAnswer(@PathVariable Long sessionId,
                                                   @Valid @RequestBody CreateStudentAnswerRequestDTO request) {
        StudentAnswerResponseDTO response = sessionService.saveStudentAnswer(sessionId, request);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
    }

    @Operation(summary = "Submit exam and calculate result")
    @PostMapping("/{sessionId}/submit")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> submitExam(@PathVariable Long sessionId) {
        ResultResponseDTO response = sessionService.calculateResultAndEndSession(sessionId);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
    }
}
