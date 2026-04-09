package com.exam.exam_system.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentCode(String studentCode);

    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByStudentCodeAndStudentIdNot(String studentCode, Long studentId);

    boolean existsByUser_UserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query(value = "SELECT s FROM Student s",
           countQuery = "SELECT COUNT(s) FROM Student s")
    Page<Student> findAllWithUser(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query(value = "SELECT s FROM Student s WHERE s.isActive = true",
           countQuery = "SELECT COUNT(s) FROM Student s WHERE s.isActive = true")
    Page<Student> findByIsActiveTrueWithUser(Pageable pageable);

    Page<Student> findByIsActiveTrue(Pageable pageable);

    long countByAcademicYear(Integer academicYear);

    long countByIsActiveTrue();

    @EntityGraph(attributePaths = {"user"})
    @Query(value = "SELECT s FROM Student s WHERE s.user.department.departmentId = :departmentId",
           countQuery = "SELECT COUNT(s) FROM Student s WHERE s.user.department.departmentId = :departmentId")
    Page<Student> findByUser_Department_DepartmentIdWithUser(@Param("departmentId") Long departmentId, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query(value = "SELECT s FROM Student s WHERE s.user.college.collegeId = :collegeId",
           countQuery = "SELECT COUNT(s) FROM Student s WHERE s.user.college.collegeId = :collegeId")
    Page<Student> findByUser_College_CollegeIdWithUser(@Param("collegeId") Long collegeId, Pageable pageable);

    Page<Student> findByUser_Department_DepartmentId(Long departmentId, Pageable pageable);

    Page<Student> findByUser_College_CollegeId(Long collegeId, Pageable pageable);

    Optional<Student> findByUser_UserId(Long userId);

    @Query("SELECT s FROM Student s JOIN FETCH s.user u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.college LEFT JOIN FETCH u.department WHERE u.userId = :userId")
    Optional<Student> findByUserIdWithFullProfile(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query(value = """
            SELECT s FROM Student s
            WHERE (:studentCode IS NULL OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :studentCode, '%')))
            AND (:academicYear IS NULL OR s.academicYear = :academicYear)
            AND (:isActive IS NULL OR s.isActive = :isActive)
            """,
           countQuery = """
            SELECT COUNT(s) FROM Student s
            WHERE (:studentCode IS NULL OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :studentCode, '%')))
            AND (:academicYear IS NULL OR s.academicYear = :academicYear)
            AND (:isActive IS NULL OR s.isActive = :isActive)
            """)
    Page<Student> searchStudents(@Param("studentCode") String studentCode,
                                 @Param("academicYear") Integer academicYear,
                                 @Param("isActive") Boolean isActive,
                                 Pageable pageable);
    

}