package com.exam.exam_system.service;

import com.exam.exam_system.dto.*;
import com.exam.exam_system.entities.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.QuestionMapper;
import com.exam.exam_system.mapper.StudentExamSessionMapper;
import com.exam.exam_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles exam session lifecycle: start session, get questions, end session.
 */
@Service
@RequiredArgsConstructor
public class ExamSessionService {

    private final StudentExamSessionRepository sessionRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final StudentAnswerRepository answerRepository;
    private final ResultRepository resultRepository;
    private final StudentExamSessionMapper sessionMapper;
    private final QuestionMapper questionMapper;
    private final ExamValidationHelper validationHelper;

    // ======================== Step 3: Start Exam Session ========================

    /**
     * Starts an exam session with full validation:
     * QR token, exam time window, student code match, no duplicate sessions.
     */
    @Transactional
    public StudentExamSessionResponseDTO startSession(StartExamRequestDTO dto) {
        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(ExamNotFoundException::new);

        // Validate QR token and exam availability
        validationHelper.validateQrToken(exam, dto.getToken());
        validationHelper.validateExamAvailability(exam);

        // Verify student code and resolve authenticated student
        Student student = validationHelper.resolveAndVerifyStudentCode(dto.getStudentCode());

        // Prevent duplicate sessions
        validationHelper.ensureNoDuplicateSession(student.getStudentId(), exam.getExamId());

        // Create session
        StudentExamSession session = StudentExamSession.builder()
                .exam(exam)
                .student(student)
                .sessionCode(UUID.randomUUID().toString())
                .isActive(true)
                .startedAt(LocalDateTime.now())
                .build();

        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    // ======================== Step 4: Get Exam Questions ========================

    /**
     * Returns exam questions for an active session.
     * - Verifies session exists, belongs to authenticated student, is active, and not expired.
     * - Uses eager-fetched query to avoid N+1 problems.
     * - If randomizeQuestions is enabled, shuffles the question order.
     * - Never returns correct answers to the student.
     */
    @Transactional(readOnly = true)
    public ExamQuestionsResponseDTO getExamQuestions(Long sessionId) {
        // Fetch session with exam eagerly loaded to avoid lazy-loading issues
        StudentExamSession session = sessionRepository.findByIdWithExam(sessionId)
                .orElseThrow(SessionNotFoundException::new);

        // Verify session ownership
        validationHelper.validateSessionOwnership(session);

        // Verify session is still active
        validationHelper.validateSessionIsActive(session);

        // Verify exam duration has not expired
        validationHelper.validateSessionNotExpired(session);

        Exam exam = session.getExam();

        // Fetch questions with choices in a single query (avoid N+1)
        List<Question> questions = questionRepository.findQuestionsWithChoices(exam.getExamId());

        // Randomize question order if exam setting requires it
        if (Boolean.TRUE.equals(exam.getRandomizeQuestions())) {
            Collections.shuffle(questions);
        }

        // Map to student view DTOs (no correct answers exposed)
        List<QuestionStudentViewDTO> questionDTOs = questions.stream()
                .map(questionMapper::toStudentViewDto)
                .collect(Collectors.toList());

        // Calculate session expiry time
        LocalDateTime expiresAt = null;
        if (exam.getDurationMinutes() != null && session.getStartedAt() != null) {
            expiresAt = session.getStartedAt().plusMinutes(exam.getDurationMinutes());
        }

        return ExamQuestionsResponseDTO.builder()
                .sessionId(session.getSessionId())
                .examId(exam.getExamId())
                .examTitle(exam.getTitle())
                .durationMinutes(exam.getDurationMinutes())
                .perQuestionTimeSeconds(exam.getPerQuestionTimeSeconds())
                .allowBackNavigation(exam.getAllowBackNavigation())
                .startedAt(session.getStartedAt())
                .expiresAt(expiresAt)
                .totalQuestions(questions.size())
                .questions(questionDTOs)
                .build();
    }

    // ======================== End Session ========================

    /**
     * Ends the exam session and calculates the result.
     * Uses optimized query to fetch answers in bulk instead of N+1 per question.
     */
    @Transactional
    public void endSession(Long sessionId) {
        StudentExamSession session = sessionRepository.findByIdWithExam(sessionId)
                .orElseThrow(SessionNotFoundException::new);

        // Verify session ownership
        validationHelper.validateSessionOwnership(session);

        // If already ended, return silently (idempotent)
        if (!Boolean.TRUE.equals(session.getIsActive())) {
            return;
        }

        session.setEndedAt(LocalDateTime.now());
        session.setIsActive(false);
        sessionRepository.save(session);

        // Calculate result using bulk-fetched answers (avoid N+1)
        List<StudentAnswer> allAnswers = answerRepository.findByStudentSession_SessionId(sessionId);

        // Fetch questions in single query
        List<Question> questions = questionRepository.findByExamExamId(session.getExam().getExamId());

        // Build a lookup map for quick answer resolution
        java.util.Map<Long, StudentAnswer> answerByQuestionId = allAnswers.stream()
                .collect(Collectors.toMap(
                        a -> a.getQuestion().getQuestionId(),
                        a -> a,
                        (a1, a2) -> a1 // in case of duplicates, keep first
                ));

        int totalMark = 0;
        int maxMark = 0;

        for (Question question : questions) {
            maxMark += question.getMarks();
            StudentAnswer answer = answerByQuestionId.get(question.getQuestionId());
            if (answer != null && Boolean.TRUE.equals(answer.getIsCorrect())) {
                totalMark += question.getMarks();
            }
        }

        Result result = Result.builder()
                .studentSession(session)
                .totalMark(totalMark)
                .maxMark(maxMark)
                .submittedAt(LocalDateTime.now())
                .build();

        resultRepository.save(result);
    }

    // ======================== Get My Exams (Student) ========================

    /**
     * Returns all exam sessions for the currently authenticated student.
     * Includes exam info, session status, and result if available.
     */
    @Transactional(readOnly = true)
    public List<StudentSessionOverviewDTO> getMyExams() {
        Student student = validationHelper.resolveAuthenticatedStudent();

        List<StudentExamSession> sessions = sessionRepository.findAllByStudentWithExam(student.getStudentId());

        // Bulk-fetch all results for these sessions in one query
        List<Long> sessionIds = sessions.stream()
                .map(StudentExamSession::getSessionId)
                .collect(Collectors.toList());

        Map<Long, Result> resultsBySessionId = resultRepository.findByStudentSession_SessionIdIn(sessionIds)
                .stream()
                .collect(Collectors.toMap(
                        r -> r.getStudentSession().getSessionId(),
                        r -> r
                ));

        return sessions.stream().map(session -> {
            Exam exam = session.getExam();
            Result result = resultsBySessionId.get(session.getSessionId());

            LocalDateTime expiresAt = null;
            if (exam.getDurationMinutes() != null && session.getStartedAt() != null) {
                expiresAt = session.getStartedAt().plusMinutes(exam.getDurationMinutes());
            }

            String status;
            if (!Boolean.TRUE.equals(session.getIsActive())) {
                status = "COMPLETED";
            } else if (expiresAt != null && LocalDateTime.now().isAfter(expiresAt)) {
                status = "EXPIRED";
            } else {
                status = "IN_PROGRESS";
            }

            return StudentSessionOverviewDTO.builder()
                    .sessionId(session.getSessionId())
                    .examId(exam.getExamId())
                    .examTitle(exam.getTitle())
                    .examDescription(exam.getDescription())
                    .subjectName(exam.getSubject().getSubjectName())
                    .durationMinutes(exam.getDurationMinutes())
                    .totalQuestions(exam.getTotalQuestions())
                    .isActive(session.getIsActive())
                    .startedAt(session.getStartedAt())
                    .endedAt(session.getEndedAt())
                    .expiresAt(expiresAt)
                    .totalMark(result != null ? result.getTotalMark() : null)
                    .maxMark(result != null ? result.getMaxMark() : null)
                    .status(status)
                    .build();
        }).collect(Collectors.toList());
    }

    // ======================== Get Session By ID (Student) ========================

    /**
     * Returns session details for a specific session owned by the authenticated student.
     * Used by the client to resume a session (e.g., after page refresh).
     */
    @Transactional(readOnly = true)
    public StudentExamSessionResponseDTO getSessionById(Long sessionId) {
        StudentExamSession session = sessionRepository.findByIdWithExam(sessionId)
                .orElseThrow(SessionNotFoundException::new);

        validationHelper.validateSessionOwnership(session);

        return sessionMapper.toResponseDTO(session);
    }

    // ======================== Legacy startSession (backward compat) ========================

    @Transactional
    @Deprecated
    public StudentExamSessionResponseDTO startSession(Long examId, String token) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(ExamNotFoundException::new);

        // Validate QR token and exam availability
        validationHelper.validateQrToken(exam, token);
        validationHelper.validateExamAvailability(exam);

        // Resolve authenticated student (no student code verification in legacy endpoint)
        Student student = validationHelper.resolveAuthenticatedStudent();

        // Prevent duplicate sessions
        validationHelper.ensureNoDuplicateSession(student.getStudentId(), exam.getExamId());

        // Create session
        StudentExamSession session = StudentExamSession.builder()
                .exam(exam)
                .student(student)
                .sessionCode(UUID.randomUUID().toString())
                .isActive(true)
                .startedAt(LocalDateTime.now())
                .build();

        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }
}