package com.exam.exam_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
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
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

	private final RoleService roleService;

	@Operation(summary = "Create new role")
	@PostMapping
	public ResponseEntity<BasicResponse> createRole(@Valid @RequestBody RoleCreateRequestDTO request) {

		RoleGetResponseDTO response = roleService.createRole(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_ROLE, response));
	}

	@Operation(summary = "Update existing role")
	@PutMapping("/{roleId}")
	public ResponseEntity<BasicResponse> updateRole(@PathVariable Long roleId,
			@Valid @RequestBody RoleUpdateRequestDTO request) {

		RoleGetResponseDTO response = roleService.updateRole(roleId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.ROLE_UPDATE, response));
	}

	@Operation(summary = "Get role by ID")
	@GetMapping("/{roleId}")
	public ResponseEntity<BasicResponse> getRoleById(@PathVariable Long roleId) {

		RoleGetResponseDTO response = roleService.getRoleById(roleId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get role by name")
	@GetMapping("/by-name")
	public ResponseEntity<BasicResponse> getRoleByName(@RequestParam String roleName) {

		RoleGetResponseDTO response = roleService.getRoleByName(roleName);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get all roles")
	@GetMapping
	public ResponseEntity<BasicResponse> getAllRoles() {

		List<RoleGetResponseDTO> roles = roleService.getAllRoles();
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, roles));
	}

	@Operation(summary = "Delete role by ID")
	@DeleteMapping("/{roleId}")
	public ResponseEntity<BasicResponse> deleteRole(@PathVariable Long roleId) {

		roleService.deleteRoleById(roleId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_ROLE));
	}
}
