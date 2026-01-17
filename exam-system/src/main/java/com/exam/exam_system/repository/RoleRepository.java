package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(String name);
	Boolean existsByRoleName(String name);
	Boolean existsByRoleNameAndRoleIdNot(String roleName, Long roleId);
}
