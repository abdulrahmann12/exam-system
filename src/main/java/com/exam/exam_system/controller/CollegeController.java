package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.service.CollegeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = SwaggerMessages.TAG_COLLEGE, description = SwaggerMessages.TAG_COLLEGE_DESC)
@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
public class CollegeController {

	private final CollegeService collegeService;

	@Operation(summary = SwaggerMessages.CREATE_COLLEGE, description = SwaggerMessages.CREATE_COLLEGE_DESC)
	@PostMapping
	@PreAuthorize("hasAuthority('COLLEGE_CREATE')")
	public ResponseEntity<BasicResponse> createCollege(@Valid @RequestBody CollegeCreateRequestDTO request) {

		CollegeGetResponseDTO response = collegeService.createCollege(request);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_COLLEGE, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_COLLEGE, description = SwaggerMessages.UPDATE_COLLEGE_DESC)
	@PutMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_UPDATE')")
	public ResponseEntity<BasicResponse> updateCollege(@PathVariable("collegeId") Long collegeId,
			@Valid @RequestBody CollegeUpdateRequestDTO request) {

		CollegeGetResponseDTO response = collegeService.updateCollege(collegeId, request);
		return ResponseEntity.ok(new BasicResponse(Messages.COLLEGE_UPDATE, response));
	}

	@Operation(summary = SwaggerMessages.GET_COLLEGE_BY_ID, description = SwaggerMessages.GET_COLLEGE_BY_ID_DESC)
	@GetMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getCollegeById(@PathVariable("collegeId") Long collegeId) {

		CollegeGetResponseDTO response = collegeService.getCollegeById(collegeId);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_COLLEGE_BY_NAME, description = SwaggerMessages.GET_COLLEGE_BY_NAME_DESC)
	@GetMapping("/by-name")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getCollegeByName(@RequestParam(name = "collegeName") String collegeName) {

		CollegeGetResponseDTO response = collegeService.getCollegeByName(collegeName);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.SEARCH_COLLEGES, description = SwaggerMessages.SEARCH_COLLEGES_DESC)
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> searchColleges(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<CollegeGetResponseDTO> colleges = collegeService.searchColleges(keyword, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, colleges));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_COLLEGES, description = SwaggerMessages.GET_ALL_COLLEGES_DESC)
	@GetMapping
	@PreAuthorize("hasAuthority('COLLEGE_READ')")
	public ResponseEntity<BasicResponse> getAllColleges(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<CollegeGetResponseDTO> colleges = collegeService.getAllColleges(page, size);
		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, colleges));
	}

	@Operation(summary = SwaggerMessages.DELETE_COLLEGE, description = SwaggerMessages.DELETE_COLLEGE_DESC)
	@DeleteMapping("/{collegeId}")
	@PreAuthorize("hasAuthority('COLLEGE_DELETE')")
	public ResponseEntity<BasicResponse> deleteCollege(@PathVariable("collegeId") Long collegeId) {

		collegeService.deleteCollegeById(collegeId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_COLLEGE));
	}
}