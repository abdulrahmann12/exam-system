package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where e.isActive = true
			""")
	Page<Exam> findAllActive(Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			""")
	Page<Exam> findAll(Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where e.isActive = true
			      and (lower(e.title) like lower(concat('%', :keyword, '%'))
			           or lower(e.description) like lower(concat('%', :keyword, '%')))
			""")
	Page<Exam> searchExams(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where c.collegeId = :collegeId
			      and e.isActive = true
			""")
	Page<Exam> findByCollegeId(@Param("collegeId") Long collegeId, Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where d.departmentId = :departmentId
			      and e.isActive = true
			""")
	Page<Exam> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where u.userId = :userId
			      and e.isActive = true
			""")
	Page<Exam> findByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query("""
			    select e from Exam e
			    join fetch e.college c
			    join fetch e.department d
			    join fetch d.college dc
			    join fetch e.subject s
			    join fetch e.createdBy u
			    where s.subjectId = :subjectId
			      and e.isActive = true
			""")
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
}
