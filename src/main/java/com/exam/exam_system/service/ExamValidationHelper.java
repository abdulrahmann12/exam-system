package com.exam.exam_system.service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.entities.Exam;
import com.exam.exam_system.entities.Student;
import com.exam.exam_system.entities.StudentExamSession;
import com.exam.exam_system.entities.User;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.repository.StudentExamSessionRepository;
import com.exam.exam_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Centralized validation helper for exam-related operations.
 * Eliminates duplicated validation logic across services.
 */
@Component
@RequiredArgsConstructor
public class ExamValidationHelper {

    private final StudentRepository studentRepository;
    private final StudentExamSessionRepository sessionRepository;
    private final UserService userService;

    // ======================== QR Token Validation ========================

    /**
     * Validates the QR token against the exam and checks expiration.
     */
    public void validateQrToken(Exam exam, String token) {
        if (!token.equals(exam.getQrToken())) {
            throw new InvalidTokenException(Messages.INVALID_QR);
        }

        if (exam.getQrExpiresAt() != null && LocalDateTime.now().isAfter(exam.getQrExpiresAt())) {
            throw new InvalidTokenException(Messages.QR_EXPIRED);
        }
    }

    // ======================== Exam Availability Validation ========================

    /**
     * Validates that the exam is active and within its time window.
     */
    public void validateExamAvailability(Exam exam) {
        if (!Boolean.TRUE.equals(exam.getIsActive())) {
            throw new ExamNotAvailableException(Messages.EXAM_NOT_ACTIVE);
        }
        validateExamTimeWindow(exam);
    }

    /**
     * Validates that the current time is within the exam's start and end time window.
     */
    public void validateExamTimeWindow(Exam exam) {
        LocalDateTime now = LocalDateTime.now();

        if (exam.getStartTime() != null && now.isBefore(exam.getStartTime())) {
            throw new ExamNotAvailableException(Messages.EXAM_NOT_STARTED_YET);
        }

        if (exam.getEndTime() != null && now.isAfter(exam.getEndTime())) {
            throw new ExamNotAvailableException(Messages.EXAM_ALREADY_ENDED);
        }
    }

    // ======================== Student Resolution & Validation ========================

    /**
     * Resolves the authenticated student. Verifies the student exists and is active.
     */
    public Student resolveAuthenticatedStudent() {
        User currentUser = userService.getCurrentUser();
        Student student = studentRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(StudentNotFoundException::new);

        if (!Boolean.TRUE.equals(student.getIsActive())) {
            throw new UnauthorizedActionException(Messages.STUDENT_ACCOUNT_DEACTIVATED);
        }

        return student;
    }

    /**
     * Resolves the authenticated student and verifies the student code matches.
     */
    public Student resolveAndVerifyStudentCode(String studentCode) {
        Student student = resolveAuthenticatedStudent();

        if (!student.getStudentCode().equals(studentCode)) {
            throw new StudentCodeMismatchException();
        }

        return student;
    }

    // ======================== Duplicate Session Check ========================

    /**
     * Ensures no existing session exists for the student and exam.
     */
    public void ensureNoDuplicateSession(Long studentId, Long examId) {
        if (sessionRepository.existsByStudent_StudentIdAndExam_ExamId(studentId, examId)) {
            throw new DuplicateExamSessionException();
        }
    }

    // ======================== Session Validation ========================

    /**
     * Validates that a session is active and has not expired based on exam duration.
     */
    public void validateSessionIsActive(StudentExamSession session) {
        if (!Boolean.TRUE.equals(session.getIsActive())) {
            throw new UnauthorizedActionException(Messages.EXAM_ALREADY_SUBMITTED);
        }
    }

    /**
     * Validates that the session has not exceeded the exam duration.
     */
    public void validateSessionNotExpired(StudentExamSession session) {
        Exam exam = session.getExam();
        if (exam.getDurationMinutes() != null && session.getStartedAt() != null) {
            LocalDateTime expiresAt = session.getStartedAt().plusMinutes(exam.getDurationMinutes());
            if (LocalDateTime.now().isAfter(expiresAt)) {
                throw new SessionExpiredException();
            }
        }
    }

    /**
     * Validates session ownership — ensures the session belongs to the authenticated student.
     */
    public Student validateSessionOwnership(StudentExamSession session) {
        User currentUser = userService.getCurrentUser();
        Student student = studentRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(StudentNotFoundException::new);

        if (!session.getStudent().getStudentId().equals(student.getStudentId())) {
            throw new UnauthorizedActionException(Messages.UNAUTHORIZED);
        }

        return student;
    }
}
