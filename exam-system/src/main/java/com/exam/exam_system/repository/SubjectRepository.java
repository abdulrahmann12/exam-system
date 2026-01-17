package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Optional<Subject> findBySubjectName(String name);
	boolean existsBySubjectName(String subjectName);
	
    Optional<Subject> findBySubjectNameAndDepartment_DepartmentId(String subjectName, Long departmentId);

    boolean existsBySubjectNameAndDepartment_DepartmentId(String subjectName, Long departmentId);

    List<Subject> findAllByDepartment_DepartmentId(Long departmentId);

    List<Subject> findAllByCollege_CollegeId(Long collegeId);

    boolean existsByCollege_CollegeId(Long collegeId);
    boolean existsByDepartment_DepartmentId(Long departmentId);
}
