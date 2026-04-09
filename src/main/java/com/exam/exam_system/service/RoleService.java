package com.exam.exam_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.dto.RoleCreateRequestDTO;
import com.exam.exam_system.dto.RoleGetResponseDTO;
import com.exam.exam_system.dto.RoleUpdateRequestDTO;
import com.exam.exam_system.entities.Permission;
import com.exam.exam_system.entities.Role;
import com.exam.exam_system.exception.PermissionNotFoundException;
import com.exam.exam_system.exception.RoleAlreadyExistsException;
import com.exam.exam_system.exception.RoleDeletionNotAllowedException;
import com.exam.exam_system.exception.RoleNotFoundException;
import com.exam.exam_system.mapper.RoleMapper;
import com.exam.exam_system.repository.PermissionRepository;
import com.exam.exam_system.repository.RoleRepository;
import com.exam.exam_system.repository.UserRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

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
	private final CachedUserDetailsService cachedUserDetailsService;

	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
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
	@CacheEvict(value = "roles", allEntries = true)
	public RoleGetResponseDTO updateRole(Long roleId, @Valid RoleUpdateRequestDTO dto) {

		Role role = roleRepository.findByIdWithPermissions(roleId).orElseThrow(RoleNotFoundException::new);

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

		// Role permission changes affect all users with this role — evict all auth cache
		cachedUserDetailsService.evictAllUserDetails();

		return roleMapper.toDto(savedRole);

	}

	@Cacheable(value = "roles", key = "#p0.toUpperCase()")
	public RoleGetResponseDTO getRoleByName(String roleName) {
		Role role = roleRepository.findByRoleNameWithPermissions(roleName.toUpperCase()).orElseThrow(RoleNotFoundException::new);
		return roleMapper.toDto(role);
	}

	@Cacheable(value = "roles", key = "#p0")
	public RoleGetResponseDTO getRoleById(Long roleId) {
		Role role = roleRepository.findByIdWithPermissions(roleId).orElseThrow(RoleNotFoundException::new);
		return roleMapper.toDto(role);
	}

	public Page<RoleGetResponseDTO> getAllRoles(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("roleName"));

		// Fetch all roles with permissions eagerly (since permissions is now LAZY)
		List<Role> allRoles = roleRepository.findAllWithPermissions();

		// Manual pagination over the pre-fetched list
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), allRoles.size());

		List<Role> pageContent = start >= allRoles.size() ? List.of() : allRoles.subList(start, end);

		Page<Role> rolesPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allRoles.size());

		return rolesPage.map(roleMapper::toDto);
	}

	@Transactional
	@CacheEvict(value = "roles", allEntries = true)
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
