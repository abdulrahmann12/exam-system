package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.College;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

	Optional<College> findByCollegeName(String name);
	Boolean existsByCollegeName(String name);
	Boolean existsByCollegeNameAndCollegeIdNot(String collegeName, Long collegeId);
	List<College> findByCollegeNameContainingIgnoreCase(String keyword);

}
