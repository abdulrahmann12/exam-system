package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.exam.exam_system.Entities.Department;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findByDepartmentName(String name);

	Boolean existsByDepartmentName(String name);

	Boolean existsByDepartmentNameAndDepartmentIdNot(String departmentName, Long departmentId);

	boolean existsByCollege_CollegeId(Long collegeId);

	Page<Department> findByDepartmentNameContainingIgnoreCase(String keyword, Pageable pageable);

	Page<Department> findByCollege_CollegeId(Long collegeId, Pageable pageable);

	boolean existsByDepartmentNameAndCollege_CollegeId(String departmentName, Long collegeId);

	boolean existsByDepartmentNameAndCollege_CollegeIdAndDepartmentIdNot(String departmentName, Long collegeId,
			Long departmentId);
}
