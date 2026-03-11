package com.exam.exam_system.repository;

import com.exam.exam_system.entities.StudentExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamSessionRepository extends JpaRepository<StudentExamSession, Long> {
    List<StudentExamSession> findByExam_ExamId(Long examId);
    Optional<StudentExamSession> findBySessionCode(String sessionCode);
}
