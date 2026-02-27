package com.exam.exam_system.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.entities.*;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.config.Messages;
import com.exam.exam_system.exception.*;
import com.exam.exam_system.repository.*;
import com.exam.exam_system.mapper.StudentExamSessionMapper;
import com.exam.exam_system.mapper.StudentAnswerMapper;
import com.exam.exam_system.mapper.ResultMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class StudentExamSessionService extends BaseService {

    private final StudentExamSessionRepository sessionRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final StudentAnswerRepository answerRepository;
    private final ResultRepository resultRepository;
    private final StudentExamSessionMapper sessionMapper;
    private final StudentAnswerMapper answerMapper;
    private final ResultMapper resultMapper;
    private final UserService userService;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    @Transactional(readOnly = true)
    public ExamResponseDTO validateTokenAndGetExam(String token) {
        Exam exam = examRepository.findByQrToken(token)
                .orElseThrow(ExamNotFoundException::new);

        if (!exam.getIsActive()) {
            throw new ExamAlreadyDeactivatedException();
        }

        if (exam.getQrExpiresAt() != null && LocalDateTime.now().isAfter(exam.getQrExpiresAt())) {
            throw new InvalidTokenException(Messages.QR_EXPIRED);
        }

        // Mapping to DTO
        return ExamResponseDTO.builder()
                .examId(exam.getExamId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .durationMinutes(exam.getDurationMinutes())
                .perQuestionTimeSeconds(exam.getPerQuestionTimeSeconds())
                .allowBackNavigation(exam.getAllowBackNavigation())
                .randomizeQuestions(exam.getRandomizeQuestions())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .isActive(exam.getIsActive())
                .build();
    }

    @Transactional
    public StudentExamSessionResponseDTO startSession(Long examId) {
        User user = userService.getCurrentUser();
        Student student = studentRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(StudentNotFoundException::new);

        Exam exam = examRepository.findById(examId)
                .orElseThrow(ExamNotFoundException::new);

        if (!exam.getIsActive()) {
            throw new ExamAlreadyDeactivatedException();
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new InvalidExamTimeException();
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new InvalidExamTimeException();
        }

        StudentExamSession session = StudentExamSession.builder()
                .exam(exam)
                .student(student)
                .sessionCode(UUID.randomUUID().toString())
                .isActive(true)
                .startedAt(now)
                .build();

        StudentExamSession savedSession = sessionRepository.save(session);
        return sessionMapper.toResponseDTO(savedSession);
    }

    @Transactional
    public StudentAnswerResponseDTO saveStudentAnswer(Long sessionId, CreateStudentAnswerRequestDTO answerDto) {
        StudentExamSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getIsActive()) {
            throw new ExamLockedException();
        }

        Question question = questionRepository.findById(answerDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (!question.getExam().getExamId().equals(session.getExam().getExamId())) {
            throw new QuestionNotBelongToExamException();
        }

        StudentAnswer studentAnswer = session.getAnswers().stream()
                .filter(a -> a.getQuestion().getQuestionId().equals(question.getQuestionId()))
                .findFirst()
                .orElse(new StudentAnswer());

        studentAnswer.setStudentSession(session);
        studentAnswer.setQuestion(question);

        if (question.getQuestionType() == QuestionType.ESSAY) {
            studentAnswer.setAnswerText(answerDto.getAnswerText());
            studentAnswer.setChoice(null);
            studentAnswer.setIsCorrect(null); // Manual grading needed for essay
        } else {
            Choice choice = choiceRepository.findById(answerDto.getChoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Choice not found"));
            if (!choice.getQuestion().getQuestionId().equals(question.getQuestionId())) {
                throw new ChoiceNotBelongToQuestionException();
            }
            studentAnswer.setChoice(choice);
            studentAnswer.setIsCorrect(choice.getIsCorrect());
            studentAnswer.setAnswerText(choice.getChoiceText());
        }

        StudentAnswer savedAnswer = answerRepository.save(studentAnswer);
        return answerMapper.toResponseDTO(savedAnswer);
    }

    @Transactional
    public ResultResponseDTO calculateResultAndEndSession(Long sessionId) {
        StudentExamSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getIsActive()) {
            throw new ExamLockedException();
        }

        int totalMark = 0;
        int maxMark = 0;

        for (Question question : session.getExam().getQuestions()) {
            maxMark += (question.getMarks() != null) ? question.getMarks() : 0;

            Optional<StudentAnswer> answerOpt = session.getAnswers().stream()
                    .filter(a -> a.getQuestion().getQuestionId().equals(question.getQuestionId()))
                    .findFirst();

            if (answerOpt.isPresent()) {
                StudentAnswer answer = answerOpt.get();
                if (Boolean.TRUE.equals(answer.getIsCorrect())) {
                    totalMark += (question.getMarks() != null) ? question.getMarks() : 0;
                }
            }
        }

        session.setIsActive(false);
        session.setEndedAt(LocalDateTime.now());
        sessionRepository.save(session);

        Result result = Result.builder()
                .studentSession(session)
                .totalMark(totalMark)
                .maxMark(maxMark)
                .submittedAt(LocalDateTime.now())
                .build();

        Result savedResult = resultRepository.save(result);
        return resultMapper.toResponseDTO(savedResult);
    }
}
