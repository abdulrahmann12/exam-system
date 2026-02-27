package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.College;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

	Optional<College> findByCollegeName(String name);

	Boolean existsByCollegeName(String name);

	Boolean existsByCollegeNameAndCollegeIdNot(String collegeName, Long collegeId);

	Page<College> findByCollegeNameContainingIgnoreCase(String keyword, Pageable pageable);

}
