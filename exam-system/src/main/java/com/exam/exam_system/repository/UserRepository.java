package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByRole_RoleId(Long roleId);

	boolean existsByPhone(String phone);

	boolean existsByPhoneAndUserIdNot(String phone, Long userId);

	Page<User> findByRole_RoleName(String roleName, Pageable pageable);

	Page<User> findByCollege_CollegeId(Long collegeId, Pageable pageable);

	Page<User> findByDepartment_DepartmentId(Long departmentId, Pageable pageable);

	boolean existsByUsernameAndUserIdNot(String username, Long userId);

	boolean existsByEmailAndUserIdNot(String email, Long userId);

	@Query(
			 value = """
			    SELECT u FROM User u
			    WHERE
			    (:keyword IS NULL OR
			     LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     u.phone LIKE CONCAT('%', :keyword, '%'))
			    AND (:role IS NULL OR u.role.roleName = :role)
			    AND (:collegeId IS NULL OR u.college.collegeId = :collegeId)
			    AND (:departmentId IS NULL OR u.department.departmentId = :departmentId)
			    AND (:isActive IS NULL OR u.isActive = :isActive)
			 """,
			 countQuery = """
			    SELECT COUNT(u) FROM User u
			    WHERE
			    (:keyword IS NULL OR
			     LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			     u.phone LIKE CONCAT('%', :keyword, '%'))
			    AND (:role IS NULL OR u.role.roleName = :role)
			    AND (:collegeId IS NULL OR u.college.collegeId = :collegeId)
			    AND (:departmentId IS NULL OR u.department.departmentId = :departmentId)
			    AND (:isActive IS NULL OR u.isActive = :isActive)
			 """
			)
			Page<User> searchUsers(
			        @Param("keyword") String keyword,
			        @Param("role") String role,
			        @Param("collegeId") Long collegeId,
			        @Param("departmentId") Long departmentId,
			        @Param("isActive") Boolean isActive,
			        Pageable pageable
			);


}
