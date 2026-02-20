package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.CollegeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "College Controller", description = "API for managing colleges")
@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
public class CollegeController {

	private final CollegeService collegeService;

	@Operation(summary = "Create new college")
	@PostMapping
	@PreAuthorize("hasAuthority('COLLEGE_CREATE')")
	public ResponseEntity<BasicResponse> createCollege(@RequestBody CollegeCreateRequestDTO request) {

		CollegeGetResponseDTO response = collegeService.createCollege(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_COLLEGE, response));
	}

	@Operation(summary = "Update existing college")
	@PutMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_UPDATE')")
	public ResponseEntity<BasicResponse> updateCollege(@PathVariable Long collegeId,
			@RequestBody CollegeUpdateRequestDTO request) {

		CollegeGetResponseDTO response = collegeService.updateCollege(collegeId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.COLLEGE_UPDATE, response));
	}

	@Operation(summary = "Get college by ID")
	@GetMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getCollegeById(@PathVariable Long collegeId) {

		CollegeGetResponseDTO response = collegeService.getCollegeById(collegeId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get college by name")
	@GetMapping("/by-name")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getCollegeByName(@RequestParam String collegeName) {

		CollegeGetResponseDTO response = collegeService.getCollegeByName(collegeName);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Search colleges by name")
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> searchColleges(@RequestParam String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<CollegeGetResponseDTO> colleges = collegeService.searchColleges(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, colleges));
	}

	@Operation(summary = "Get all colleges")
	@GetMapping
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getAllColleges(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<CollegeGetResponseDTO> colleges = collegeService.getAllColleges(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, colleges));
	}

	@Operation(summary = "Delete college by ID")
	@DeleteMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_DELETE')")
	public ResponseEntity<BasicResponse> deleteCollege(@PathVariable Long collegeId) {

		collegeService.deleteCollegeById(collegeId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_COLLEGE));
	}
}
