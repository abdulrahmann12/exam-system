package com.exam.exam_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.exam.exam_system.Entities.Permission;
import com.exam.exam_system.dto.PermissionCreateRequestDTO;
import com.exam.exam_system.dto.PermissionGetResponseDTO;
import com.exam.exam_system.dto.PermissionUpdateRequestDTO;
import com.exam.exam_system.exception.PermissionAlreadyExistsException;
import com.exam.exam_system.exception.PermissionNotFoundException;
import com.exam.exam_system.mapper.PermissionMapper;
import com.exam.exam_system.repository.PermissionRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;

	@Transactional
	public PermissionGetResponseDTO createPermission(@Valid PermissionCreateRequestDTO dto) {

		String code = dto.getModule().name().toUpperCase() + "_" + dto.getAction().name().toUpperCase();

		if (permissionRepository.existsByCode(code)) {
			throw new PermissionAlreadyExistsException();
		}

		Permission permission = permissionMapper.toEntity(dto);
		permission.setCode(code);
		Permission saved = permissionRepository.save(permission);
		return permissionMapper.toDto(saved);
	}

	@Transactional
	public PermissionGetResponseDTO updatePermission(Long permissionId, @Valid PermissionUpdateRequestDTO dto) {
		String code = dto.getModule().name().toUpperCase() + "_" + dto.getAction().name().toUpperCase();

		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(PermissionNotFoundException::new);

		if (permissionRepository.existsByCode(code) && !permission.getCode().equals(code)) {
			throw new PermissionAlreadyExistsException();
		}

		permission.setCode(code);
		permission.setDescription(dto.getDescription());
		permission.setActive(dto.getActive());

		Permission saved = permissionRepository.save(permission);
		return permissionMapper.toDto(saved);
	}

	public PermissionGetResponseDTO getPermissionById(Long permissionId) {
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(PermissionNotFoundException::new);
		return permissionMapper.toDto(permission);
	}

	public List<PermissionGetResponseDTO> getAllPermissions() {
		List<Permission> permissions = permissionRepository.findAll();
		return permissions.stream().map(permissionMapper::toDto).toList();
	}

	@Transactional
	public void deletePermissionById(Long permissionId) {
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(PermissionNotFoundException::new);

		if (!permission.getActive()) {
			throw new PermissionNotFoundException();
		}
		permission.setActive(false);
		permissionRepository.save(permission);
	}
}
