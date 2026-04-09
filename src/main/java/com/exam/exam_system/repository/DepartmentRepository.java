package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	@EntityGraph(attributePaths = {"college"})
	@Query("SELECT d FROM Department d WHERE d.departmentId = :id")
	Optional<Department> findByIdWithCollege(@Param("id") Long id);

	@EntityGraph(attributePaths = {"college"})
	@Query("SELECT d FROM Department d WHERE d.departmentName = :name")
	Optional<Department> findByDepartmentName(@Param("name") String name);

	Boolean existsByDepartmentName(String name);

	Boolean existsByDepartmentNameAndDepartmentIdNot(String departmentName, Long departmentId);

	boolean existsByCollege_CollegeId(Long collegeId);

	@EntityGraph(attributePaths = {"college"})
	Page<Department> findByDepartmentNameContainingIgnoreCase(String keyword, Pageable pageable);

	@EntityGraph(attributePaths = {"college"})
	Page<Department> findByCollege_CollegeId(Long collegeId, Pageable pageable);

	@EntityGraph(attributePaths = {"college"})
	@Query(value = "SELECT d FROM Department d",
	       countQuery = "SELECT COUNT(d) FROM Department d")
	Page<Department> findAllWithCollege(Pageable pageable);

	boolean existsByDepartmentNameAndCollege_CollegeId(String departmentName, Long collegeId);

	boolean existsByDepartmentNameAndCollege_CollegeIdAndDepartmentIdNot(String departmentName, Long collegeId,
			Long departmentId);
}
