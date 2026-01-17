package com.exam.exam_system.controller;

import java.util.List;

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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> createDepartment(@RequestBody DepartmentCreateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.createDepartment(request);

		return ResponseEntity.ok(new BasicResponse(Messages.ADD_DEPARTMENT, response));
	}

	@Operation(summary = "Update existing department")
	@PutMapping("/{departmentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> updateDepartment(@PathVariable Long departmentId,
			@RequestBody DepartmentUpdateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.updateDepartment(departmentId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.DEPARTMENT_UPDATE, response));
	}

	@Operation(summary = "Get department by ID")
	@GetMapping("/{departmentId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getDepartmentById(@PathVariable Long departmentId) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get department by name")
	@GetMapping("/by-name")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getDepartmentByName(@RequestParam String departmentName) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentByName(departmentName);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Search departments by name")
	@GetMapping("/search")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> searchDepartments(@RequestParam String keyword) {

		List<DepartmentGetResponseDTO> departments = departmentService.searchDepartments(keyword);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Get all departments")
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getAllDepartments() {

		List<DepartmentGetResponseDTO> departments = departmentService.getAllDepartments();

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Get all departments by college ID")
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getDepartmentsByCollegeId(@PathVariable Long collegeId) {

		List<DepartmentGetResponseDTO> departments = departmentService.getDepartmentsByCollegeId(collegeId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = "Delete department by ID")
	@DeleteMapping("/{departmentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteDepartment(@PathVariable Long departmentId) {

		departmentService.deleteDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_DEPARTMENT));
	}
}
