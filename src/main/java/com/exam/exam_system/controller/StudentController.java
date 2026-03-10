package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Student Controller", description = "API for managing students")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	/* ==================== CREATE / REGISTER ==================== */
	@Operation(summary = SwaggerMessages.REGISTER_STUDENT)
	@PostMapping("/register")
	public ResponseEntity<BasicResponse> registerStudent(@Valid @RequestBody StudentRegisterRequestDTO request) {
		StudentProfileResponseDTO response = studentService.registerStudentAndReturnProfile(request);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_CREATED, response));
	}

	/* ==================== UPDATE ==================== */
	@Operation(summary = SwaggerMessages.UPDATE_MY_STUDENT_PROFILE)
	@PutMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> updateMyProfile(@Valid @RequestBody StudentUpdateProfileRequestDTO request) {
		StudentProfileResponseDTO response = studentService.updateMyProfile(request);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_UPDATED, response));
	}

	@Operation(summary = SwaggerMessages.ADMIN_UPDATE_STUDENT)
	@PutMapping("/{studentId}")
	@PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
	public ResponseEntity<BasicResponse> adminUpdateStudent(@PathVariable("studentId") Long studentId,
			@Valid @RequestBody StudentUpdateRequestDTO request) {
		StudentGetResponseDTO response = studentService.updateStudent(studentId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_UPDATED, response));
	}

	/* ==================== GET ==================== */
	@Operation(summary = SwaggerMessages.GET_STUDENT_BY_ID)
	@GetMapping("/{studentId}")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getStudentById(@PathVariable("studentId") Long studentId) {
		StudentGetResponseDTO response = studentService.getStudentById(studentId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_STUDENTS)
	@GetMapping
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getAllStudents(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<StudentGetResponseDTO> students = studentService.getAllStudents(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_ACTIVE_STUDENTS)
	@GetMapping("/active")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getAllActiveStudents(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<StudentGetResponseDTO> students = studentService.getAllActiveStudents(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
	}

	@Operation(summary = SwaggerMessages.GET_MY_STUDENT_PROFILE)
	@GetMapping("/me")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getMyProfile() {
		StudentProfileResponseDTO response = studentService.getMyProfile();
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	/* ==================== DELETE / DEACTIVATE / ACTIVATE ==================== */
	@Operation(summary = SwaggerMessages.DELETE_STUDENT)
	@DeleteMapping("/{studentId}/delete")
	@PreAuthorize("hasAuthority('STUDENTS_DELETE')")
	public ResponseEntity<BasicResponse> deleteStudent(@PathVariable("studentId") Long studentId) {
		studentService.deleteStudent(studentId);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_DELETED));
	}

	@Operation(summary = SwaggerMessages.DEACTIVATE_STUDENT)
	@DeleteMapping("/{studentId}/deactivate")
	@PreAuthorize("hasAuthority('STUDENTS_DELETE')")
	public ResponseEntity<BasicResponse> deactivateStudent(@PathVariable("studentId") Long studentId) {
		studentService.deactivateStudent(studentId);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_DEACTIVATED));
	}

	@Operation(summary = SwaggerMessages.ACTIVATE_STUDENT)
	@PostMapping("/{studentId}/activate")
	@PreAuthorize("hasAuthority('STUDENTS_UPDATE')")
	public ResponseEntity<BasicResponse> activateStudent(@PathVariable("studentId") Long studentId) {
		studentService.activateStudent(studentId);
		return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_ACTIVATED));
	}

	/* ==================== SEARCH ==================== */
	@Operation(summary = SwaggerMessages.SEARCH_STUDENTS)
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> searchStudents(
			@RequestParam(name = "studentCode", required = false) String studentCode,
			@RequestParam(name = "academicYear", required = false) Integer academicYear,
			@RequestParam(name = "isActive", required = false) Boolean isActive,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<StudentGetResponseDTO> students = studentService.searchStudents(studentCode, academicYear, isActive, page,
				size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
	}

	@Operation(summary = SwaggerMessages.GET_STUDENTS_BY_DEPARTMENT)
	@GetMapping("/department/{departmentId}")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getStudentsByDepartment(@PathVariable("departmentId") Long departmentId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<StudentGetResponseDTO> students = studentService.getStudentsByDepartment(departmentId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
	}

	@Operation(summary = SwaggerMessages.GET_STUDENTS_BY_COLLEGE)
	@GetMapping("/college/{collegeId}")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> getStudentsByCollege(@PathVariable("collegeId") Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<StudentGetResponseDTO> students = studentService.getStudentsByCollege(collegeId, page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
	}

	@Operation(summary = SwaggerMessages.COUNT_STUDENTS_BY_YEAR)
	@GetMapping("/count/year")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> countStudentsByYear(@RequestParam("year") Integer year) {
		long count = studentService.countStudentsByYear(year);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, count));
	}

	@Operation(summary = SwaggerMessages.COUNT_ACTIVE_STUDENTS)
	@GetMapping("/count/active")
	@PreAuthorize("hasAuthority('STUDENTS_READ')")
	public ResponseEntity<BasicResponse> countActiveStudents() {
		long count = studentService.countActiveStudents();
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, count));
	}
}
