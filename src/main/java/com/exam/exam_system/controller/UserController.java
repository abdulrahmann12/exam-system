package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.entities.User;
import com.exam.exam_system.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User Controller", description = "API for managing users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/* ==================== PROFILE ==================== */

	@Operation(summary = SwaggerMessages.GET_CURRENT_USER_PROFILE)
	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getProfile() {

		UserProfileResponseDTO response = userService.getProfile();

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.UPDATE_CURRENT_USER_PROFILE)
	@PutMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> updateProfile(@AuthenticationPrincipal User user,
			@Valid @RequestBody UserUpdateProfileRequestDTO request) {

		UserProfileResponseDTO response = userService.updateProfile(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.PROFILE_UPDATED, response));
	}

	/* ==================== USERNAME ==================== */

	@Operation(summary = SwaggerMessages.CHANGE_USERNAME)
	@PutMapping("/profile/username")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> changeUsername(@AuthenticationPrincipal User user,
			@Valid @RequestBody ChangeUsernameRequestDTO request) {

		userService.changeUsername(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.USERNAME_UPDATED));
	}

	/* ==================== EMAIL ==================== */

	@Operation(summary = SwaggerMessages.REQUEST_EMAIL_CHANGE)
	@PostMapping("/profile/email/request")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> requestEmailChange(@AuthenticationPrincipal User user,
			@Valid @RequestBody ChangeEmailRequestDTO request) {

		userService.requestEmailChange(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.EMAIL_CHANGE_REQUESTED));
	}

	@Operation(summary = SwaggerMessages.CONFIRM_EMAIL_CHANGE)
	@PostMapping("/profile/email/confirm")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> confirmEmailChange(@AuthenticationPrincipal User user,
			@Valid @RequestBody VerifyEmailChangeRequestDTO request) {

		userService.confirmEmailChange(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.EMAIL_CHANGED));
	}

	/* ==================== ADMIN ==================== */

	@Operation(summary = SwaggerMessages.GET_USER_BY_ID)
	@GetMapping("/{userId:\\d+}")
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> getUserById(@PathVariable("userId") Long userId) {

		UserResponseDTO response = userService.getUserById(userId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = SwaggerMessages.GET_ALL_USERS)
	@GetMapping
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getAllUsers(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = SwaggerMessages.GET_USERS_BY_COLLEGE)
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> getUsersByCollege(@PathVariable("collegeId") Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByCollege(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = SwaggerMessages.GET_USERS_BY_DEPARTMENT)
	@GetMapping("/by-department/{departmentId}")
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> getUsersByDepartment(@PathVariable("departmentId") Long departmentId,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByDepartment(departmentId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = SwaggerMessages.GET_USERS_BY_ROLE)
	@GetMapping("/by-role")
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> getUsersByRole(@RequestParam("roleName") String roleName,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByRole(roleName, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = SwaggerMessages.UPDATE_USER_BY_ADMIN)
	@PutMapping("/{userId:\\d+}")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	public ResponseEntity<BasicResponse> adminUpdateUser(@PathVariable("userId") Long userId,
			@Valid @RequestBody AdminUserUpdateRequestDTO request) {

		UserResponseDTO response = userService.adminUpdateUser(userId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.UPDATE_USER, response));
	}

	@Operation(summary = SwaggerMessages.DEACTIVATE_USER)
	@DeleteMapping("/{userId:\\d+}")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	public ResponseEntity<BasicResponse> deactivateUser(@PathVariable("userId") Long userId) {

		userService.deactivateUser(userId);

		return ResponseEntity.ok(new BasicResponse(Messages.DEACTIVATE_USER));
	}

	@Operation(summary = SwaggerMessages.ACTIVATE_USER)
	@PutMapping("/{userId:\\d+}/activate")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	public ResponseEntity<BasicResponse> activateUser(@PathVariable("userId") Long userId) {

		userService.activateUser(userId);

		return ResponseEntity.ok(new BasicResponse(Messages.ACTIVATE_USER));
	}

	/* ==================== SEARCH ==================== */

	@Operation(summary = SwaggerMessages.SEARCH_USERS)
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('USER_READ')")
	public ResponseEntity<BasicResponse> searchUsers(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "role", required = false) String role, @RequestParam(name = "collegeId", required = false) Long collegeId,
			@RequestParam(name = "departmentId", required = false) Long departmentId, @RequestParam(name = "isActive", required = false) Boolean isActive,
			@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.searchUsers(keyword, role, collegeId, departmentId, isActive, page,
				size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}
}