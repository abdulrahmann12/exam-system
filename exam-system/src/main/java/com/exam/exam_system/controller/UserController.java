package com.exam.exam_system.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.Entities.User;
import com.exam.exam_system.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User Controller", description = "API for managing users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/* ==================== PROFILE ==================== */

	@Operation(summary = "Get current user profile")
	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> getProfile(@AuthenticationPrincipal User user) {

		UserProfileResponseDTO response = userService.getProfile(user);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Update current user profile")
	@PutMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> updateProfile(@AuthenticationPrincipal User user,
			@RequestBody UserUpdateProfileRequestDTO request) {

		UserProfileResponseDTO response = userService.updateProfile(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.PROFILE_UPDATED, response));
	}

	/* ==================== USERNAME ==================== */

	@Operation(summary = "Change username")
	@PutMapping("/profile/username")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> changeUsername(@AuthenticationPrincipal User user,
			@RequestBody ChangeUsernameRequestDTO request) {

		userService.changeUsername(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.USERNAME_UPDATED));
	}

	/* ==================== EMAIL ==================== */

	@Operation(summary = "Request email change")
	@PostMapping("/profile/email/request")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> requestEmailChange(@AuthenticationPrincipal User user,
			@RequestBody ChangeEmailRequestDTO request) {

		userService.requestEmailChange(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.EMAIL_CHANGE_REQUESTED));
	}

	@Operation(summary = "Confirm email change")
	@PostMapping("/profile/email/confirm")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> confirmEmailChange(@AuthenticationPrincipal User user,
			@RequestBody VerifyEmailChangeRequestDTO request) {

		userService.confirmEmailChange(user, request);

		return ResponseEntity.ok(new BasicResponse(Messages.EMAIL_CHANGED));
	}

	/* ==================== ADMIN ==================== */

	@Operation(summary = "Get user by ID")
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> getUserById(@PathVariable Long userId) {

		UserResponseDTO response = userService.getUserById(userId);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, response));
	}

	@Operation(summary = "Get all users")
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getAllUsers(page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = "Get users by college")
	@GetMapping("/by-college/{collegeId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> getUsersByCollege(@PathVariable Long collegeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByCollege(collegeId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = "Get users by department")
	@GetMapping("/by-department/{departmentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> getUsersByDepartment(@PathVariable Long departmentId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByDepartment(departmentId, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = "Get users by role")
	@GetMapping("/by-role/{roleName}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> getUsersByRole(@PathVariable String roleName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.getUsersByRole(roleName, page, size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}

	@Operation(summary = "Update user by admin")
	@PutMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> adminUpdateUser(@PathVariable Long userId,
			@RequestBody AdminUserUpdateRequestDTO request) {

		UserResponseDTO response = userService.adminUpdateUser(userId, request);

		return ResponseEntity.ok(new BasicResponse(Messages.UPDATE_USER, response));
	}

	@Operation(summary = "Deactivate user")
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deactivateUser(@PathVariable Long userId) {

		userService.deactivateUser(userId);

		return ResponseEntity.ok(new BasicResponse(Messages.DEACTIVATE_USER));
	}

	/* ==================== SEARCH ==================== */

	@Operation(summary = "Search users")
	@GetMapping("/search")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> searchUsers(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String role, @RequestParam(required = false) Long collegeId,
			@RequestParam(required = false) Long departmentId, @RequestParam(required = false) Boolean isActive,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<UserResponseDTO> users = userService.searchUsers(keyword, role, collegeId, departmentId, isActive, page,
				size);

		return ResponseEntity.ok(new BasicResponse(Messages.FETCH_SUCCESS, users));
	}
}
