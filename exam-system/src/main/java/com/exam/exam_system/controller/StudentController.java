package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
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

    /* ==================== CREATE ==================== */
    @Operation(summary = "Create new student")
    @PostMapping
    @PreAuthorize("hasAuthority('STUDENT_CREATE')")
    public ResponseEntity<BasicResponse> createStudent(@Valid @RequestBody StudentCreateRequestDTO request) {
        StudentGetResponseDTO response = studentService.createStudent(request);
        return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_CREATED, response));
    }

    /* ==================== UPDATE ==================== */
    @Operation(summary = "Update student by ID")
    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('STUDENT_UPDATE')")
    public ResponseEntity<BasicResponse> updateStudent(@PathVariable Long studentId,
                                                       @Valid @RequestBody StudentUpdateRequestDTO request) {
        StudentGetResponseDTO response = studentService.updateStudent(studentId, request);
        return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_UPDATED, response));
    }

    /* ==================== GET ==================== */
    @Operation(summary = "Get student by ID")
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> getStudentById(@PathVariable Long studentId) {
        StudentGetResponseDTO response = studentService.getStudentById(studentId);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
    }

    @Operation(summary = "Get all students (paginated)")
    @GetMapping
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<StudentGetResponseDTO> students = studentService.getAllStudents(page, size);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
    }

    @Operation(summary = "Get all active students (paginated)")
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> getAllActiveStudents(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<StudentGetResponseDTO> students = studentService.getAllActiveStudents(page, size);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
    }

    /* ==================== DELETE / DEACTIVATE ==================== */
    @Operation(summary = "Delete student by ID (hard delete)")
    @DeleteMapping("/{studentId}/delete")
    @PreAuthorize("hasAuthority('STUDENT_DELETE')")
    public ResponseEntity<BasicResponse> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_DELETED));
    }

    @Operation(summary = "Deactivate student by ID (soft delete)")
    @DeleteMapping("/{studentId}/deactivate")
    @PreAuthorize("hasAuthority('STUDENT_DELETE')")
    public ResponseEntity<BasicResponse> deactivateStudent(@PathVariable Long studentId) {
        studentService.deactivateStudent(studentId);
        return ResponseEntity.ok(new BasicResponse(Messages.STUDENT_DEACTIVATED));
    }

    /* ==================== SEARCH ==================== */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('STUDENT_READ')")
    public ResponseEntity<BasicResponse> searchStudents(
            @RequestParam(required = false) String studentCode,
            @RequestParam(required = false) Integer academicYear,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<StudentGetResponseDTO> students = studentService.searchStudents(studentCode, academicYear, isActive, page, size);
        return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, students));
    }
}