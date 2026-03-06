package com.exam.exam_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.StudentExamSession;

import java.util.List;

@Repository
public interface StudentExamSessionRepository extends JpaRepository<StudentExamSession, Long> {
    List<StudentExamSession> findByExam_ExamId(Long examId);
}
