package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.BasicResponse;
import com.exam.exam_system.dto.PermissionCreateRequestDTO;
import com.exam.exam_system.dto.PermissionGetResponseDTO;
import com.exam.exam_system.dto.PermissionUpdateRequestDTO;
import com.exam.exam_system.entities.PermissionActions;
import com.exam.exam_system.entities.PermissionModules;
import com.exam.exam_system.service.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = SwaggerMessages.TAG_PERMISSION, description = SwaggerMessages.TAG_PERMISSION_DESC)
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@Operation(summary = SwaggerMessages.CREATE_PERMISSION, description = SwaggerMessages.CREATE_PERMISSION_DESC)
	@PostMapping
	@PreAuthorize("hasAuthority('PERMISSION_CREATE')")
	public ResponseEntity<BasicResponse> createPermission(@Valid @RequestBody PermissionCreateRequestDTO request) {
		PermissionGetResponseDTO response = permissionService.createPermission(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_PERMISSION, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_PERMISSION, description = SwaggerMessages.UPDATE_PERMISSION_DESC)
	@PutMapping("/{permissionId}")
	@PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
	public ResponseEntity<BasicResponse> updatePermission(@PathVariable("permissionId") Long permissionId,
			@Valid @RequestBody PermissionUpdateRequestDTO request) {
		PermissionGetResponseDTO response = permissionService.updatePermission(permissionId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.PERMISSION_UPDATE, response));
	}

	@Operation(summary = SwaggerMessages.GET_PERMISSION_BY_ID, description = SwaggerMessages.GET_PERMISSION_BY_ID_DESC)
	@GetMapping("/{permissionId}")
	@PreAuthorize("hasAuthority('PERMISSION_READ')")
	public ResponseEntity<BasicResponse> getPermissionById(@PathVariable("permissionId") Long permissionId) {
		PermissionGetResponseDTO response = permissionService.getPermissionById(permissionId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_PERMISSIONS, description = SwaggerMessages.GET_ALL_PERMISSIONS_DESC)
	@GetMapping
	@PreAuthorize("hasAuthority('PERMISSION_READ')")
	public ResponseEntity<BasicResponse> getAllPermissions(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<PermissionGetResponseDTO> permissions = permissionService.getAllPermissions(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, permissions));
	}

	@Operation(summary = SwaggerMessages.DELETE_PERMISSION, description = SwaggerMessages.DELETE_PERMISSION_DESC)
	@DeleteMapping("/{permissionId}")
	@PreAuthorize("hasAuthority('PERMISSION_DELETE')")
	public ResponseEntity<BasicResponse> deletePermission(@PathVariable("permissionId") Long permissionId) {
		permissionService.deletePermissionById(permissionId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_PERMISSION));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_PERMISSION_MODULES, description = SwaggerMessages.GET_ALL_PERMISSION_MODULES_DESC)
	@GetMapping("/modules")
	@PreAuthorize("hasAuthority('PERMISSION_READ')")
	public List<String> getModules() {
		return Arrays.stream(PermissionModules.values()).map(Enum::name).collect(Collectors.toList());
	}

	@Operation(summary = SwaggerMessages.GET_ALL_PERMISSION_ACTIONS, description = SwaggerMessages.GET_ALL_PERMISSION_ACTIONS_DESC)
	@GetMapping("/actions")
	@PreAuthorize("hasAuthority('PERMISSION_READ')")
	public List<String> getActions() {
		return Arrays.stream(PermissionActions.values()).map(Enum::name).collect(Collectors.toList());
	}

}