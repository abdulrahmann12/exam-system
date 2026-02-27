package com.exam.exam_system.service;

import com.exam.exam_system.dto.CreateStudentAnswerRequestDTO;
import com.exam.exam_system.dto.StudentExamSessionResponseDTO;
import com.exam.exam_system.entities.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.mapper.StudentExamSessionMapper;
import com.exam.exam_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentExamSessionService {

    private final StudentExamSessionRepository sessionRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final StudentAnswerRepository answerRepository;
    private final ResultRepository resultRepository;
    private final StudentExamSessionMapper sessionMapper;
    private final UserService userService;

    public Exam validateTokenAndGetExam(String token) {
        return examRepository.findByQrToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid exam token"));
    }

    @Transactional
    public StudentExamSessionResponseDTO startSession(Long examId, String token) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(ExamNotFoundException::new);

        if (!token.equals(exam.getQrToken())) {
            throw new InvalidTokenException("Invalid exam token");
        }

        if (exam.getQrExpiresAt() != null && LocalDateTime.now().isAfter(exam.getQrExpiresAt())) {
            throw new InvalidTokenException("Exam token has expired");
        }

        User currentUser = userService.getCurrentUser();
        Student student = studentRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(StudentNotFoundException::new);

        StudentExamSession session = StudentExamSession.builder()
                .exam(exam)
                .student(student)
                .sessionCode(UUID.randomUUID().toString())
                .isActive(true)
                .startedAt(LocalDateTime.now())
                .build();

        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    @Transactional
    public void submitAnswer(CreateStudentAnswerRequestDTO dto) {
        StudentExamSession session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getIsActive()) {
            throw new UnauthorizedActionException("Session is not active");
        }

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        StudentAnswer answer = answerRepository.findByStudentSession_SessionIdAndQuestion_QuestionId(
                session.getSessionId(), question.getQuestionId())
                .orElse(new StudentAnswer());

        answer.setStudentSession(session);
        answer.setQuestion(question);
        answer.setAnsweredAt(LocalDateTime.now());

        if (question.getQuestionType() == QuestionType.MCQ || question.getQuestionType() == QuestionType.TRUE_FALSE) {
            if (dto.getChoiceId() == null) {
                throw new UnauthorizedActionException("Choice ID is required for this question type");
            }
            Choice choice = choiceRepository.findById(dto.getChoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Choice not found"));
            answer.setChoice(choice);
            answer.setIsCorrect(choice.getIsCorrect());
        } else {
            answer.setAnswerText(dto.getAnswerText());
            // For essay questions, we don't automatically mark as correct/incorrect
            answer.setIsCorrect(null);
        }

        answerRepository.save(answer);
    }

    @Transactional
    public void endSession(Long sessionId) {
        StudentExamSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getIsActive()) {
            return;
        }

        session.setEndedAt(LocalDateTime.now());
        session.setIsActive(false);
        sessionRepository.save(session);

        // Calculate Result
        int totalMark = 0;
        int maxMark = 0;

        for (Question q : session.getExam().getQuestions()) {
            maxMark += q.getMarks();
            StudentAnswer studentAnswer = answerRepository.findByStudentSession_SessionIdAndQuestion_QuestionId(sessionId, q.getQuestionId())
                    .orElse(null);

            if (studentAnswer != null && Boolean.TRUE.equals(studentAnswer.getIsCorrect())) {
                totalMark += q.getMarks();
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
}
