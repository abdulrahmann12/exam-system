package com.exam.exam_system.service;

import com.exam.exam_system.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @deprecated This class is retained for backward compatibility.
 * Use {@link ExamAccessService}, {@link ExamSessionService}, and {@link ExamAnswerService} instead.
 */
@Deprecated
@Service
@RequiredArgsConstructor
public class StudentExamSessionService {

    private final ExamAccessService examAccessService;
    private final ExamSessionService examSessionService;
    private final ExamAnswerService examAnswerService;

    public ExamAccessResponseDTO accessExamViaQr(String token) {
        return examAccessService.accessExamViaQr(token);
    }

    public ExamAccessResponseDTO verifyStudentCode(VerifyStudentCodeRequestDTO dto) {
        return examAccessService.verifyStudentCode(dto);
    }

    public StudentExamSessionResponseDTO startSession(StartExamRequestDTO dto) {
        return examSessionService.startSession(dto);
    }

    public StudentExamSessionResponseDTO startSession(Long examId, String token) {
        return examSessionService.startSession(examId, token);
    }

    public void submitAnswer(CreateStudentAnswerRequestDTO dto) {
        examAnswerService.submitAnswer(dto);
    }

    public void endSession(Long sessionId) {
        examSessionService.endSession(sessionId);
    }
}