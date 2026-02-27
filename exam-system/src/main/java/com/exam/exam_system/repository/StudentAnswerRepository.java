package com.exam.exam_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.StudentAnswer;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByStudentSession_SessionId(Long sessionId);
    List<StudentAnswer> findByQuestion_QuestionId(Long questionId);
    Optional<StudentAnswer> findByStudentSession_SessionIdAndQuestion_QuestionId(Long sessionId, Long questionId);
}
