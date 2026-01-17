package com.exam.exam_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.Role;
import com.exam.exam_system.dto.RoleCreateRequestDTO;
import com.exam.exam_system.dto.RoleGetResponseDTO;
import com.exam.exam_system.dto.RoleUpdateRequestDTO;
import com.exam.exam_system.exception.RoleAlreadyExistsException;
import com.exam.exam_system.exception.RoleDeletionNotAllowedException;
import com.exam.exam_system.exception.RoleNotFoundException;
import com.exam.exam_system.mapper.RoleMapper;
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

	@Transactional
	public RoleGetResponseDTO createRole(@Valid RoleCreateRequestDTO roleCreateRequestDTO) {

		if (roleRepository.existsByRoleName(roleCreateRequestDTO.getRoleName())) {
			throw new RoleAlreadyExistsException();
		}
		Role role = roleMapper.toEntity(roleCreateRequestDTO);

		Role savedRole = roleRepository.save(role);
		return roleMapper.toDto(savedRole);

	}

	@Transactional
	public RoleGetResponseDTO updateRole(Long roleId, @Valid RoleUpdateRequestDTO roleCreateRequestDTO) {

		Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);

		if (roleRepository.existsByRoleNameAndRoleIdNot(roleCreateRequestDTO.getRoleName(), roleId)) {
			throw new RoleAlreadyExistsException();
		}

		role.setRoleName(roleCreateRequestDTO.getRoleName());
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
