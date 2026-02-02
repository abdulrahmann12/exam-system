package com.exam.exam_system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.Permission;
import com.exam.exam_system.Entities.Role;
import com.exam.exam_system.dto.RoleCreateRequestDTO;
import com.exam.exam_system.dto.RoleGetResponseDTO;
import com.exam.exam_system.dto.RoleUpdateRequestDTO;
import com.exam.exam_system.exception.PermissionNotFoundException;
import com.exam.exam_system.exception.RoleAlreadyExistsException;
import com.exam.exam_system.exception.RoleDeletionNotAllowedException;
import com.exam.exam_system.exception.RoleNotFoundException;
import com.exam.exam_system.mapper.RoleMapper;
import com.exam.exam_system.repository.PermissionRepository;
import com.exam.exam_system.repository.RoleRepository;
import com.exam.exam_system.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;
	private final RoleMapper roleMapper;
	private final UserRepository userRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public RoleGetResponseDTO createRole(@Valid RoleCreateRequestDTO dto) {

		if (roleRepository.existsByRoleName(dto.getRoleName())) {
			throw new RoleAlreadyExistsException();
		}

		Role role = new Role();
		role.setRoleName(dto.getRoleName());

		if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {

			List<Permission> permissions = permissionRepository.findAllById(dto.getPermissionIds());

			Set<Long> foundIds = permissions.stream().map(Permission::getPermissionId).collect(Collectors.toSet());

			List<Long> missingIds = dto.getPermissionIds().stream().filter(id -> !foundIds.contains(id)).toList();

			if (!missingIds.isEmpty()) {
				throw new PermissionNotFoundException(missingIds);
			}

			role.setPermissions(new HashSet<>(permissions));
		}

		return roleMapper.toDto(roleRepository.save(role));
	}

	@Transactional
	public RoleGetResponseDTO updateRole(Long roleId, @Valid RoleUpdateRequestDTO dto) {

		Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);

		if (roleRepository.existsByRoleNameAndRoleIdNot(dto.getRoleName(), roleId)) {
			throw new RoleAlreadyExistsException();
		}
		if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {

			List<Permission> permissions = permissionRepository.findAllById(dto.getPermissionIds());

			Set<Long> foundIds = permissions.stream().map(Permission::getPermissionId).collect(Collectors.toSet());

			List<Long> missingIds = dto.getPermissionIds().stream().filter(id -> !foundIds.contains(id)).toList();

			if (!missingIds.isEmpty()) {
				throw new PermissionNotFoundException(missingIds);
			}

			role.setPermissions(new HashSet<>(permissions));
		}

		role.setRoleName(dto.getRoleName());
		Role savedRole = roleRepository.save(role);
		return roleMapper.toDto(savedRole);

	}

	public RoleGetResponseDTO getRoleByName(String roleName) {
		Role role = roleRepository.findByRoleName(roleName.toUpperCase()).orElseThrow(RoleNotFoundException::new);
		return roleMapper.toDto(role);
	}

	public RoleGetResponseDTO getRoleById(Long roleId) {
		Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
		return roleMapper.toDto(role);
	}

	public List<RoleGetResponseDTO> getAllRoles() {
		List<Role> roles = roleRepository.findAll();
		return roles.stream().map(roleMapper::toDto).toList();
	}

	@Transactional
	public void deleteRoleById(Long roleId) {
		if (!roleRepository.existsById(roleId)) {
			throw new RoleNotFoundException();
		}
		if (userRepository.existsByRole_RoleId(roleId)) {
			throw new RoleDeletionNotAllowedException();
		}
		roleRepository.deleteById(roleId);
	}
}
