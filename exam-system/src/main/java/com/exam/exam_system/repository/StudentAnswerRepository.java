package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByStudentSession_SessionId(Long sessionId);
    List<StudentAnswer> findByQuestion_QuestionId(Long questionId);
}
