package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Department Controller", description = "API for managing departments")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@Operation(summary = SwaggerMessages.CREATE_DEPARTMENT)
	@PostMapping
	@PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
	public ResponseEntity<BasicResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.createDepartment(request);

		return ResponseEntity.ok(new BasicResponse(Messages.ADD_DEPARTMENT, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_DEPARTMENT)
	@PutMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
	public ResponseEntity<BasicResponse> updateDepartment(@PathVariable("departmentId") Long departmentId,
			@Valid @RequestBody DepartmentUpdateRequestDTO request) {

		DepartmentGetResponseDTO response = departmentService.updateDepartment(departmentId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.DEPARTMENT_UPDATE, response));
	}

	@Operation(summary = SwaggerMessages.GET_DEPARTMENT_BY_ID)
	@GetMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentById(@PathVariable("departmentId") Long departmentId) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_DEPARTMENT_BY_NAME)
	@GetMapping("/by-name")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentByName(@RequestParam(name = "departmentName") String departmentName) {

		DepartmentGetResponseDTO response = departmentService.getDepartmentByName(departmentName);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.SEARCH_DEPARTMENTS)
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> searchDepartments(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.searchDepartments(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_DEPARTMENTS)
	@GetMapping
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getAllDepartments(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.getAllDepartments(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = SwaggerMessages.GET_DEPARTMENTS_BY_COLLEGE)
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_READ')")
	public ResponseEntity<BasicResponse> getDepartmentsByCollegeId(@PathVariable("collegeId") Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<DepartmentGetResponseDTO> departments = departmentService.getDepartmentsByCollegeId(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, departments));
	}

	@Operation(summary = SwaggerMessages.DELETE_DEPARTMENT)
	@DeleteMapping("/{departmentId}")
	@PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
	public ResponseEntity<BasicResponse> deleteDepartment(@PathVariable("departmentId") Long departmentId) {

		departmentService.deleteDepartmentById(departmentId);

		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_DEPARTMENT));
	}
}