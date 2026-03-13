package com.exam.exam_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.StudentExamSession;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamSessionRepository extends JpaRepository<StudentExamSession, Long> {
    List<StudentExamSession> findByExam_ExamId(Long examId);

    boolean existsByStudent_StudentIdAndExam_ExamId(Long studentId, Long examId);

    Optional<StudentExamSession> findByStudent_StudentIdAndExam_ExamId(Long studentId, Long examId);

    /**
     * Fetch session with exam eagerly loaded to avoid N+1 and LazyInitializationException.
     */
    @Query("""
            SELECT s FROM StudentExamSession s
            JOIN FETCH s.exam
            JOIN FETCH s.student
            WHERE s.sessionId = :sessionId
            """)
    Optional<StudentExamSession> findByIdWithExam(@Param("sessionId") Long sessionId);

    /**
     * Fetch all sessions for a student with exam and subject eagerly loaded.
     * Used for "My Exams" listing.
     */
    @Query("""
            SELECT s FROM StudentExamSession s
            JOIN FETCH s.exam e
            JOIN FETCH e.subject
            JOIN FETCH s.student
            WHERE s.student.studentId = :studentId
            ORDER BY s.startedAt DESC
            """)
    List<StudentExamSession> findAllByStudentWithExam(@Param("studentId") Long studentId);
}
