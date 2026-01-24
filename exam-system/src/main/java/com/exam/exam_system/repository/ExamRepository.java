package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCollege_CollegeId(Long collegeId);
    List<Exam> findByDepartment_DepartmentId(Long departmentId);
    List<Exam> findBySubject_SubjectId(Long subjectId);
    List<Exam> findByCreatedBy_UserId(Long userId);
    boolean existsBySubject_SubjectId(Long subjectId);
}
