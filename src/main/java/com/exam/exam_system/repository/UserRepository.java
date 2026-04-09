package com.exam.exam_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u JOIN FETCH u.role r LEFT JOIN FETCH r.permissions WHERE u.username = :username")
	Optional<User> findByUsernameWithPermissions(@Param("username") String username);

	@Query("SELECT u FROM User u JOIN FETCH u.role r LEFT JOIN FETCH r.permissions WHERE u.email = :email")
	Optional<User> findByEmailWithPermissions(@Param("email") String email);

	@EntityGraph(attributePaths = {"role", "college", "department"})
	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	Optional<User> findByIdWithDetails(@Param("userId") Long userId);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByRole_RoleId(Long roleId);

	boolean existsByPhone(String phone);

	boolean existsByPhoneAndUserIdNot(String phone, Long userId);

	@EntityGraph(attributePaths = {"role", "college", "department"})
	@Query("SELECT u FROM User u WHERE u.role.roleName = :roleName")
	Page<User> findByRole_RoleName(@Param("roleName") String roleName, Pageable pageable);

	@EntityGraph(attributePaths = {"role", "college", "department"})
	@Query("SELECT u FROM User u WHERE u.college.collegeId = :collegeId")
	Page<User> findByCollege_CollegeId(@Param("collegeId") Long collegeId, Pageable pageable);

	@EntityGraph(attributePaths = {"role", "college", "department"})
	@Query("SELECT u FROM User u WHERE u.department.departmentId = :departmentId")
	Page<User> findByDepartment_DepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

	boolean existsByUsernameAndUserIdNot(String username, Long userId);

	boolean existsByEmailAndUserIdNot(String email, Long userId);

	@EntityGraph(attributePaths = {"role", "college", "department"})
	@Query(value = "SELECT u FROM User u",
	       countQuery = "SELECT COUNT(u) FROM User u")
	Page<User> findAllWithDetails(Pageable pageable);

	@EntityGraph(attributePaths = {"role", "college", "department"})
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
