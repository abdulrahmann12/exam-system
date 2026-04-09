package com.exam.exam_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.entities.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(String name);
	Boolean existsByRoleName(String name);
	Boolean existsByRoleNameAndRoleIdNot(String roleName, Long roleId);

	@Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.roleId = :roleId")
	Optional<Role> findByIdWithPermissions(@Param("roleId") Long roleId);

	@Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.roleName = :name")
	Optional<Role> findByRoleNameWithPermissions(@Param("name") String name);

	@Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions")
	List<Role> findAllWithPermissions();
}
