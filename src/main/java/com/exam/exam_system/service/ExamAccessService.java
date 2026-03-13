package com.exam.exam_system.service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.ExamAccessResponseDTO;
import com.exam.exam_system.dto.VerifyStudentCodeRequestDTO;
import com.exam.exam_system.entities.Exam;
import com.exam.exam_system.entities.Student;
import com.exam.exam_system.exception.ExamNotFoundException;
import com.exam.exam_system.exception.InvalidTokenException;
import com.exam.exam_system.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles exam access flow: QR code scanning and student code verification.
 * This is Steps 1 and 2 of the exam flow.
 */
@Service
@RequiredArgsConstructor
public class ExamAccessService {

    private final ExamRepository examRepository;
    private final ExamValidationHelper validationHelper;

    // ======================== Step 1: Access Exam via QR ========================

    /**
     * Validates QR token and returns exam details.
     * Checks that the exam exists, is active, QR token is valid, and the exam is within its time window.
     */
    @Transactional(readOnly = true)
    public ExamAccessResponseDTO accessExamViaQr(String token) {
        Exam exam = examRepository.findByQrToken(token)
                .orElseThrow(() -> new InvalidTokenException(Messages.INVALID_QR));

        validationHelper.validateExamAvailability(exam);

        if (exam.getQrExpiresAt() != null && java.time.LocalDateTime.now().isAfter(exam.getQrExpiresAt())) {
            throw new InvalidTokenException(Messages.QR_EXPIRED);
        }

        return buildExamAccessResponse(exam);
    }

    // ======================== Step 2: Verify Student Code ========================

    /**
     * Verifies that the student code belongs to the currently authenticated user.
     * Also re-validates the QR token and exam availability.
     */
    @Transactional(readOnly = true)
    public ExamAccessResponseDTO verifyStudentCode(VerifyStudentCodeRequestDTO dto) {
        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(ExamNotFoundException::new);

        // Validate QR token and exam availability
        validationHelper.validateQrToken(exam, dto.getToken());
        validationHelper.validateExamAvailability(exam);

        // Verify student code matches the authenticated user
        Student student = validationHelper.resolveAndVerifyStudentCode(dto.getStudentCode());

        // Check for duplicate session
        validationHelper.ensureNoDuplicateSession(student.getStudentId(), exam.getExamId());

        return buildExamAccessResponse(exam);
    }

    // ======================== Helper ========================

    private ExamAccessResponseDTO buildExamAccessResponse(Exam exam) {
        return ExamAccessResponseDTO.builder()
                .examId(exam.getExamId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .durationMinutes(exam.getDurationMinutes())
                .totalQuestions(exam.getTotalQuestions())
                .allowBackNavigation(exam.getAllowBackNavigation())
                .randomizeQuestions(exam.getRandomizeQuestions())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .isCurrentlyAvailable(true)
                .build();
    }
}
