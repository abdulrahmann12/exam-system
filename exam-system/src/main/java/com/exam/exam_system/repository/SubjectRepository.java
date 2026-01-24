package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Optional<Subject> findBySubjectName(String name);

	boolean existsBySubjectName(String subjectName);

	List<Subject> findBySubjectNameContainingIgnoreCase(String keyword);

	Optional<Subject> findBySubjectNameAndDepartment_DepartmentId(String subjectName, Long departmentId);

	boolean existsBySubjectNameAndDepartment_DepartmentId(String subjectName, Long departmentId);

	List<Subject> findAllByDepartment_DepartmentId(Long departmentId);

	boolean existsBySubjectCode(String subjectCode);

	boolean existsBySubjectCodeAndSubjectIdNot(String subjectCode, Long subjectId);

	List<Subject> findAllByCollege_CollegeId(Long collegeId);

	boolean existsByCollege_CollegeId(Long collegeId);

	boolean existsByDepartment_DepartmentId(Long departmentId);

	boolean existsBySubjectNameAndDepartment_DepartmentIdAndSubjectIdNot(String subjectName, Long departmentId,
			Long subjectId);

	List<Subject> findBySubjectCodeContainingIgnoreCase(String keyword);

	List<Subject> findBySubjectNameContainingIgnoreCaseOrSubjectCodeContainingIgnoreCase(String nameKeyword,
			String codeKeyword);
	
    @Query("""
            SELECT s FROM Subject s
            JOIN FETCH s.department
            JOIN FETCH s.college
        """)
        List<Subject> findAllWithDepartmentAndCollege();

        @Query("""
            SELECT s FROM Subject s
            JOIN FETCH s.department
            JOIN FETCH s.college
            WHERE s.subjectId = :subjectId
        """)
        Optional<Subject> findByIdWithRelations(@Param("subjectId") Long subjectId);

        @Query("""
            SELECT s FROM Subject s
            JOIN FETCH s.department
            JOIN FETCH s.college
            WHERE s.department.departmentId = :departmentId
        """)
        List<Subject> findAllByDepartmentIdWithRelations(@Param("departmentId") Long departmentId);

        @Query("""
            SELECT s FROM Subject s
            JOIN FETCH s.department
            JOIN FETCH s.college
            WHERE s.college.collegeId = :collegeId
        """)
        List<Subject> findAllByCollegeIdWithRelations(@Param("collegeId") Long collegeId);

        @Query("""
            SELECT s FROM Subject s
            JOIN FETCH s.department
            JOIN FETCH s.college
            WHERE LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
        List<Subject> searchByNameOrCodeWithRelations(@Param("keyword") String keyword);

}
