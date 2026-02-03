package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.BasicResponse;
import com.exam.exam_system.dto.PermissionCreateRequestDTO;
import com.exam.exam_system.dto.PermissionGetResponseDTO;
import com.exam.exam_system.dto.PermissionUpdateRequestDTO;
import com.exam.exam_system.service.PermissionService;
import com.exam.exam_system.Entities.PermissionActions;
import com.exam.exam_system.Entities.PermissionModules;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Permission Controller", description = "API for managing system permissions")
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PERMISSION_CREATE')")
public class PermissionController {

	private final PermissionService permissionService;

	@Operation(summary = "Create new permission")
	@PostMapping
	public ResponseEntity<BasicResponse> createPermission(@RequestBody PermissionCreateRequestDTO request) {
		PermissionGetResponseDTO response = permissionService.createPermission(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_PERMISSION, response));
	}

	@Operation(summary = "Update existing permission")
	@PutMapping("/{permissionId}")
	public ResponseEntity<BasicResponse> updatePermission(@PathVariable Long permissionId,
			@RequestBody PermissionUpdateRequestDTO request) {
		PermissionGetResponseDTO response = permissionService.updatePermission(permissionId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.PERMISSION_UPDATE, response));
	}

	@Operation(summary = "Get permission by ID")
	@GetMapping("/{permissionId}")
	public ResponseEntity<BasicResponse> getPermissionById(@PathVariable Long permissionId) {
		PermissionGetResponseDTO response = permissionService.getPermissionById(permissionId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get all permissions")
	@GetMapping
	public ResponseEntity<BasicResponse> getAllPermissions(@RequestParam(value = "0") int page,
			@RequestParam(value = "10") int size) {
		Page<PermissionGetResponseDTO> permissions = permissionService.getAllPermissions(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, permissions));
	}

	@Operation(summary = "Delete permission by ID")
	@DeleteMapping("/{permissionId}")
	public ResponseEntity<BasicResponse> deletePermission(@PathVariable Long permissionId) {
		permissionService.deletePermissionById(permissionId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_PERMISSION));
	}

	@Operation(summary = "Get all permissions modules")
	@GetMapping("/modules")
	public List<String> getModules() {
		return Arrays.stream(PermissionModules.values()).map(Enum::name).collect(Collectors.toList());
	}

	@Operation(summary = "Get all permissions actions")
	@GetMapping("/actions")
	public List<String> getActions() {
		return Arrays.stream(PermissionActions.values()).map(Enum::name).collect(Collectors.toList());
	}

}
