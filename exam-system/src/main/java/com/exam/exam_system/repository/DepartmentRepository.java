package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Department;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findByDepartmentName(String name);
}
