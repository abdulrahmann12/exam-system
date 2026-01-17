package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Department;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findByDepartmentName(String name);

	Boolean existsByDepartmentName(String name);

	Boolean existsByDepartmentNameAndDepartmentIdNot(String departmentName, Long departmentId);

	List<Department> findByDepartmentNameContainingIgnoreCase(String keyword);

	boolean existsByCollege_CollegeId(Long collegeId);

	List<Department> findByCollege_CollegeId(Long collegeId);

	boolean existsByDepartmentNameAndCollege_CollegeId(String departmentName, Long collegeId);

	boolean existsByDepartmentNameAndCollege_CollegeIdAndDepartmentIdNot(String departmentName, Long collegeId,
			Long departmentId);
}
