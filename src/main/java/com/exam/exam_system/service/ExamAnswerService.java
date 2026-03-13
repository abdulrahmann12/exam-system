package com.exam.exam_system.service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.CreateStudentAnswerRequestDTO;
import com.exam.exam_system.entities.*;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Handles answer submission for exam sessions.
 * Includes validation for session ownership, session expiry, choice-question association, and question types.
 */
@Service
@RequiredArgsConstructor
public class ExamAnswerService {

    private final StudentExamSessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final StudentAnswerRepository answerRepository;
    private final ExamValidationHelper validationHelper;

    // ======================== Submit Answer ========================

    /**
     * Submits or updates an answer for a question within an active session.
     * <p>
     * Validations performed:
     * - Session exists and belongs to authenticated student
     * - Session is still active
     * - Session has not exceeded exam duration
     * - Question belongs to the exam associated with the session
     * - For MCQ/TRUE_FALSE: choice is required and must belong to the question
     * - For ESSAY: answer text is expected (choice is ignored)
     */
    @Transactional
    public void submitAnswer(CreateStudentAnswerRequestDTO dto) {
        StudentExamSession session = sessionRepository.findByIdWithExam(dto.getSessionId())
                .orElseThrow(SessionNotFoundException::new);

        // Verify session ownership
        validationHelper.validateSessionOwnership(session);

        // Verify session is active
        validationHelper.validateSessionIsActive(session);

        // Verify exam duration has not expired (anti-cheating: prevent late submissions)
        validationHelper.validateSessionNotExpired(session);

        // Fetch and validate question
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(Messages.QUESTION_NOT_FOUND));

        // Security: ensure the question belongs to this exam
        if (!question.getExam().getExamId().equals(session.getExam().getExamId())) {
            throw new QuestionNotBelongToExamException();
        }

        // Find existing answer or create new one
        StudentAnswer answer = answerRepository
                .findByStudentSession_SessionIdAndQuestion_QuestionId(session.getSessionId(), question.getQuestionId())
                .orElse(new StudentAnswer());

        answer.setStudentSession(session);
        answer.setQuestion(question);
        answer.setAnsweredAt(LocalDateTime.now());

        if (question.getQuestionType() == QuestionType.MCQ || question.getQuestionType() == QuestionType.TRUE_FALSE) {
            if (dto.getChoiceId() == null) {
                throw new IllegalArgumentException("Choice ID is required for " + question.getQuestionType() + " questions");
            }

            Choice choice = choiceRepository.findById(dto.getChoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException(Messages.ANSWER_NOT_FOUND));

            // Security: ensure the choice belongs to this specific question
            if (!choice.getQuestion().getQuestionId().equals(question.getQuestionId())) {
                throw new ChoiceNotBelongToQuestionException();
            }

            answer.setChoice(choice);
            answer.setIsCorrect(choice.getIsCorrect());
            answer.setAnswerText(null); // clear any previous essay text
        } else {
            // ESSAY question
            answer.setAnswerText(dto.getAnswerText());
            answer.setChoice(null);     // clear any previous choice
            answer.setIsCorrect(null);  // essay questions require manual grading
        }

        answerRepository.save(answer);
    }
}
