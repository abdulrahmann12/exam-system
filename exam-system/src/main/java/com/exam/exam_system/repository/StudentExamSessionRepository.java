package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.StudentExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamSessionRepository extends JpaRepository<StudentExamSession, Long> {
    List<StudentExamSession> findByExam_ExamId(Long examId);
}
