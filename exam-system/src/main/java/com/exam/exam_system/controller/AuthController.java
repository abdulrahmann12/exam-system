package com.exam.exam_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.exam_system.Entities.User;
import com.exam.exam_system.config.Messages;
import com.exam.exam_system.dto.*;

import com.exam.exam_system.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth Controller", description = "API for user authentication and authorization (login, resst password, etc).")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AuthenticationManager authenticationManager;

	@Operation(summary = "User login", description = "Authenticate user using username/email and password and return access and refresh tokens.")
	@PostMapping("/login")
	public ResponseEntity<BasicResponse> login(@RequestBody LoginRequestDTO loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
		AuthResponse authResponse = authService.login(loginRequest);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGIN_SUCCESS, authResponse));
	}

	@Operation(summary = "Refresh access token", description = "Generate a new access token using a valid refresh token from the request.")
	@PostMapping("/refresh-token")
	public ResponseEntity<BasicResponse> refreshToken(HttpServletRequest request) {
		AuthResponse authResponse = authService.refreshToken(request);
		return ResponseEntity.ok(new BasicResponse(Messages.NEW_TOKEN_GENERATED, authResponse));
	}

	@Operation(summary = "Logout user", description = "Invalidate the current refresh token to log the user out.")
	@PostMapping("/logout")
	public ResponseEntity<BasicResponse> logout(HttpServletRequest request) {
		authService.logoutByRequest(request);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGOUT_SUCCESS));
	}

	@Operation(summary = "Create new user", description = "Admin creates a new user account with a specific role.")
	@PostMapping("/create-user")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	public ResponseEntity<BasicResponse> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
		UserResponseDTO userResponseDTO = authService.register(createUserRequestDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.CREATE_NEW_USER, userResponseDTO));
	}

	@Operation(summary = "Change user password", description = "Authenticated user changes their current password.")
	@PostMapping("/change-password")
	public ResponseEntity<BasicResponse> changePassword(@AuthenticationPrincipal User user,
			@RequestBody UserChangePasswordRequestDTO request) {
		authService.changePassword(user, request);
		return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}

	@Operation(summary = "Regenerate verification code", description = "Send a new verification code to the user’s email.")
	@PostMapping("/regenerate-code")
	public ResponseEntity<BasicResponse> regenerateCode(@AuthenticationPrincipal User user) {

		authService.reGenerateCode(user);
		return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}

	@Operation(summary = "Forget password", description = "Send a reset code to the user's registered email address.")
	@PostMapping("/forget-password")
	public ResponseEntity<BasicResponse> forgetPassword(@RequestBody EmailRequestDTO email) {
		authService.forgetPassword(email);
		return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}

	@Operation(summary = "Reset password", description = "Reset the password using the verification code sent via email.")
	@PostMapping("/reset-password")
	public ResponseEntity<BasicResponse> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswodDTO) {
		authService.resetPassword(resetPasswodDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}
}
