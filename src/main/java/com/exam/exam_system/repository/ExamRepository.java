package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.Exam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
	List<Exam> findByCollege_CollegeId(Long collegeId);

	List<Exam> findByDepartment_DepartmentId(Long departmentId);

	List<Exam> findBySubject_SubjectId(Long subjectId);

	List<Exam> findByCreatedBy_UserId(Long userId);

	boolean existsBySubject_SubjectId(Long subjectId);

	Optional<Exam> findByExamIdAndIsActiveTrue(Long examId);

	Page<Exam> findByIsActiveTrue(Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college
			    join fetch e.department d
			    join fetch d.college
			    join fetch e.subject
			    join fetch e.createdBy
			    where e.examId = :examId
			      and e.isActive = true
			""")
	Optional<Exam> findExamFull(@Param("examId") Long examId);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e where e.isActive = true",
	       countQuery = "select count(e) from Exam e where e.isActive = true")
	Page<Exam> findAllActive(Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e",
	       countQuery = "select count(e) from Exam e")
	Page<Exam> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = """
			    select e from Exam e
			    where e.isActive = true
			      and (lower(e.title) like lower(concat('%', :keyword, '%'))
			           or lower(e.description) like lower(concat('%', :keyword, '%')))
			""",
	       countQuery = """
			    select count(e) from Exam e
			    where e.isActive = true
			      and (lower(e.title) like lower(concat('%', :keyword, '%'))
			           or lower(e.description) like lower(concat('%', :keyword, '%')))
			""")
	Page<Exam> searchExams(@Param("keyword") String keyword, Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e where e.college.collegeId = :collegeId and e.isActive = true",
	       countQuery = "select count(e) from Exam e where e.college.collegeId = :collegeId and e.isActive = true")
	Page<Exam> findByCollegeId(@Param("collegeId") Long collegeId, Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e where e.department.departmentId = :departmentId and e.isActive = true",
	       countQuery = "select count(e) from Exam e where e.department.departmentId = :departmentId and e.isActive = true")
	Page<Exam> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e where e.createdBy.userId = :userId and e.isActive = true",
	       countQuery = "select count(e) from Exam e where e.createdBy.userId = :userId and e.isActive = true")
	Page<Exam> findByUserId(@Param("userId") Long userId, Pageable pageable);

	@EntityGraph(attributePaths = {"college", "department", "department.college", "subject", "createdBy"})
	@Query(value = "select e from Exam e where e.subject.subjectId = :subjectId and e.isActive = true",
	       countQuery = "select count(e) from Exam e where e.subject.subjectId = :subjectId and e.isActive = true")
	Page<Exam> findBySubjectId(@Param("subjectId") Long subjectId, Pageable pageable);

	@Query("""
		    select distinct e from Exam e
		    join fetch e.college
		    join fetch e.department
		    join fetch e.subject
		    join fetch e.createdBy
		    left join fetch e.questions q
		    left join fetch q.choices
		    where e.examId = :examId
		""")
	Optional<Exam> findExamWithQuestionsAndChoices(@Param("examId") Long examId);

	Optional<Exam> findByQrToken(String qrToken);
}
