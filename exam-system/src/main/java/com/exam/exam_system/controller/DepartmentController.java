package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Department Controller", description = "API for managing departments")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@Operation(summary = "Create new department")
	@PostMapping
	@PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
	public ResponseEntity<BasicResponse> createDepartment(@RequestBody DepartmentCreateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.createDepartment(request);

		return ResponseEntity.ok(new BasicResponse(Messages.ADD_DEPARTMENT, response));
	}

	@Operation(summary = "Update existing department")
	@PutMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
	public ResponseEntity<BasicResponse> updateDepartment(@PathVariable Long departmentId,
			@RequestBody DepartmentUpdateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.updateDepartment(departmentId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.DEPARTMENT_UPDATE, response));
	}

	@Operation(summary = "Get department by ID")
	@GetMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentById(@PathVariable Long departmentId) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get department by name")
	@GetMapping("/by-name")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentByName(@RequestParam String departmentName) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentByName(departmentName);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Search departments by name")
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> searchDepartments(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.searchDepartments(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Get all departments")
	@GetMapping
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getAllDepartments(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.getAllDepartments(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Get all departments by college ID")
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentsByCollegeId(@PathVariable Long collegeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.getDepartmentsByCollegeId(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Delete department by ID")
	@DeleteMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
	public ResponseEntity<BasicResponse> deleteDepartment(@PathVariable Long departmentId) {

		departmentService.deleteDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_DEPARTMENT));
	}
}
