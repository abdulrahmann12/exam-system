package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Role Controller", description = "API for managing system roles")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@Operation(summary = SwaggerMessages.CREATE_ROLE)
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CREATE')")
	public ResponseEntity<BasicResponse> createRole(@Valid @RequestBody RoleCreateRequestDTO request) {

		RoleGetResponseDTO response = roleService.createRole(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_ROLE, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_ROLE)
	@PutMapping("/{roleId}")
	@PreAuthorize("hasAuthority('ROLE_UPDATE')")
	public ResponseEntity<BasicResponse> updateRole(@PathVariable("roleId") Long roleId,
			@Valid @RequestBody RoleUpdateRequestDTO request) {

		RoleGetResponseDTO response = roleService.updateRole(roleId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.ROLE_UPDATE, response));
	}

	@Operation(summary = SwaggerMessages.GET_ROLE_BY_ID)
	@GetMapping("/{roleId}")
	@PreAuthorize("hasAuthority('ROLE_READ')")
	public ResponseEntity<BasicResponse> getRoleById(@PathVariable("roleId") Long roleId) {

		RoleGetResponseDTO response = roleService.getRoleById(roleId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ROLE_BY_NAME)
	@GetMapping("/by-name")
	@PreAuthorize("hasAuthority('ROLE_READ')")
	public ResponseEntity<BasicResponse> getRoleByName(@RequestParam(name = "roleName") String roleName) {

		RoleGetResponseDTO response = roleService.getRoleByName(roleName);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_ROLES)
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_READ')")
	public ResponseEntity<BasicResponse> getAllRoles(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<RoleGetResponseDTO> roles = roleService.getAllRoles(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, roles));
	}

	@Operation(summary = SwaggerMessages.DELETE_ROLE)
	@DeleteMapping("/{roleId}")
	@PreAuthorize("hasAuthority('ROLE_DELETE')")
	public ResponseEntity<BasicResponse> deleteRole(@PathVariable("roleId") Long roleId) {

		roleService.deleteRoleById(roleId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_ROLE));
	}
}