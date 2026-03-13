package com.exam.exam_system.controller;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.ExamAccessService;
import com.exam.exam_system.service.ExamAnswerService;
import com.exam.exam_system.service.ExamSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Student Exam Session Controller", description = "API for student exam access and session management")
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudentExamSessionController {

    private final ExamAccessService examAccessService;
    private final ExamSessionService examSessionService;
    private final ExamAnswerService examAnswerService;

    // ======================== Step 1: Access Exam via QR ========================

    @Operation(summary = SwaggerMessages.ACCESS_EXAM_VIA_QR)
    @GetMapping("/access")
    public ResponseEntity<BasicResponse> accessExamViaQr(@RequestParam(name = "token") String token) {
        ExamAccessResponseDTO examAccess = examAccessService.accessExamViaQr(token);
        return ResponseEntity.ok(new BasicResponse(Messages.EXAM_VALIDATED, examAccess));
    }

    // ======================== Step 2: Verify Student Code ========================

    @Operation(summary = SwaggerMessages.VERIFY_STUDENT_CODE)
    @PostMapping("/verify-student")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> verifyStudentCode(@Valid @RequestBody VerifyStudentCodeRequestDTO dto) {
        ExamAccessResponseDTO examAccess = examAccessService.verifyStudentCode(dto);
        return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_CODE_VERIFIED, examAccess));
    }

    // ======================== Step 3: Start Exam ========================

    @Operation(summary = SwaggerMessages.START_EXAM_SESSION)
    @PostMapping("/start")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> startExam(@Valid @RequestBody StartExamRequestDTO dto) {
        StudentExamSessionResponseDTO session = examSessionService.startSession(dto);
        return ResponseEntity.ok(new BasicResponse(Messages.EXAM_SESSION_STARTED_SUCCESS, session));
    }

    // ======================== Get My Exams (Student) ========================

    @Operation(summary = SwaggerMessages.GET_MY_EXAM_SESSIONS)
    @GetMapping("/my-exams")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> getMyExams() {
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, examSessionService.getMyExams()));
    }

    // ======================== Get Session By ID ========================

    @Operation(summary = SwaggerMessages.GET_SESSION_BY_ID)
    @GetMapping("/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> getSessionById(@PathVariable("sessionId") Long sessionId) {
        StudentExamSessionResponseDTO session = examSessionService.getSessionById(sessionId);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, session));
    }

    // ======================== Step 4: Get Exam Questions ========================

    @Operation(summary = SwaggerMessages.GET_SESSION_QUESTIONS)
    @GetMapping("/{sessionId}/questions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> getExamQuestions(@PathVariable("sessionId") Long sessionId) {
        ExamQuestionsResponseDTO questions = examSessionService.getExamQuestions(sessionId);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, questions));
    }

    // ======================== Submit Answer ========================

    @Operation(summary = SwaggerMessages.SUBMIT_ANSWER)
    @PostMapping("/submit-answer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> submitAnswer(@Valid @RequestBody CreateStudentAnswerRequestDTO answerDto) {
        examAnswerService.submitAnswer(answerDto);
        return ResponseEntity.ok(new BasicResponse(Messages.ANSWER_SAVED));
    }

    // ======================== End Session ========================

    @Operation(summary = SwaggerMessages.END_EXAM_SESSION)
    @PostMapping("/end/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> endSession(@PathVariable("sessionId") Long sessionId) {
        examSessionService.endSession(sessionId);
        return ResponseEntity.ok(new BasicResponse(Messages.EXAM_SUBMITTED));
    }

    // ======================== Legacy: Start session (backward compat) ========================

    @Deprecated
    @Operation(summary = SwaggerMessages.START_EXAM_SESSION)
    @PostMapping("/start/{examId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> startSession(@PathVariable("examId") Long examId, @RequestParam(name = "token") String token) {
        StudentExamSessionResponseDTO session = examSessionService.startSession(examId, token);
        return ResponseEntity.ok(new BasicResponse(Messages.EXAM_SESSION_STARTED_SUCCESS, session));
    }
}