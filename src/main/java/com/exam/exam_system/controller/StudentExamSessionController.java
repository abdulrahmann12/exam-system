package com.exam.exam_system.controller;

import com.exam.exam_system.dto.BasicResponse;
import com.exam.exam_system.dto.CreateStudentAnswerRequestDTO;
import com.exam.exam_system.dto.StudentExamSessionResponseDTO;
import com.exam.exam_system.entities.Exam;
import com.exam.exam_system.mapper.ExamMapper;
import com.exam.exam_system.service.StudentExamSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Student Exam Session Controller", description = "API for student exam session management")
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudentExamSessionController {

    private final StudentExamSessionService sessionService;
    private final ExamMapper examMapper;

    @Operation(summary = "Validate QR token and get exam details")
    @GetMapping("/enter")
    public ResponseEntity<BasicResponse> enterExam(@RequestParam(name = "token") String token) {
        Exam exam = sessionService.validateTokenAndGetExam(token);
        return ResponseEntity.ok(new BasicResponse("Exam validated successfully", examMapper.toStudentViewDto(exam)));
    }

    @Operation(summary = "Start a new exam session")
    @PostMapping("/start/{examId}")
    public ResponseEntity<BasicResponse> startSession(@PathVariable("examId") Long examId, @RequestParam(name = "token") String token) {
        StudentExamSessionResponseDTO session = sessionService.startSession(examId, token);
        return ResponseEntity.ok(new BasicResponse("Session started successfully", session));
    }

    @Operation(summary = "Submit an answer for a question")
    @PostMapping("/submit-answer")
    public ResponseEntity<BasicResponse> submitAnswer(@Valid @RequestBody CreateStudentAnswerRequestDTO answerDto) {
        sessionService.submitAnswer(answerDto);
        return ResponseEntity.ok(new BasicResponse("Answer submitted successfully"));
    }

    @Operation(summary = "End the exam session and calculate results")
    @PostMapping("/end/{sessionId}")
    public ResponseEntity<BasicResponse> endSession(@PathVariable("sessionId") Long sessionId) {
        sessionService.endSession(sessionId);
        return ResponseEntity.ok(new BasicResponse("Exam session ended successfully"));
    }
}