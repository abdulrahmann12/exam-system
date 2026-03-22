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

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.config.SwaggerMessages;
import com.exam.exam_system.dto.*;
import com.exam.exam_system.entities.User;
import com.exam.exam_system.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = SwaggerMessages.TAG_AUTH, description = SwaggerMessages.TAG_AUTH_DESC)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AuthenticationManager authenticationManager;

	@Operation(summary = SwaggerMessages.USER_LOGIN, description = SwaggerMessages.USER_LOGIN_DESC)
	@PostMapping("/login")
	public ResponseEntity<BasicResponse> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
		AuthResponse authResponse = authService.login(loginRequest);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGIN_SUCCESS, authResponse));
	}

	@Operation(summary = SwaggerMessages.REFRESH_ACCESS_TOKEN, description = SwaggerMessages.REFRESH_ACCESS_TOKEN_DESC)
	@PostMapping("/refresh-token")
	public ResponseEntity<BasicResponse> refreshToken(HttpServletRequest request) {
		AuthResponse authResponse = authService.refreshToken(request);
		return ResponseEntity.ok(new BasicResponse(Messages.NEW_TOKEN_GENERATED, authResponse));
	}

	@Operation(summary = SwaggerMessages.LOGOUT_USER, description = SwaggerMessages.LOGOUT_USER_DESC)
	@PostMapping("/logout")
	public ResponseEntity<BasicResponse> logout(HttpServletRequest request) {
		authService.logoutByRequest(request);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGOUT_SUCCESS));
	}

	@Operation(summary = SwaggerMessages.CREATE_NEW_USER, description = SwaggerMessages.CREATE_NEW_USER_DESC)
	@PostMapping("/create-user")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	public ResponseEntity<BasicResponse> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
		UserResponseDTO userResponseDTO = authService.register(createUserRequestDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.CREATE_NEW_USER, userResponseDTO));
	}

	@Operation(summary = SwaggerMessages.CHANGE_USER_PASSWORD, description = SwaggerMessages.CHANGE_USER_PASSWORD_DESC)
	@PostMapping("/change-password")
	public ResponseEntity<BasicResponse> changePassword(@AuthenticationPrincipal User user,
			@Valid @RequestBody UserChangePasswordRequestDTO request) {
		authService.changePassword(user, request);
		return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}

	@Operation(summary = SwaggerMessages.REGENERATE_VERIFICATION_CODE, description = SwaggerMessages.REGENERATE_VERIFICATION_CODE_DESC)
	@PostMapping("/regenerate-code")
	public ResponseEntity<BasicResponse> regenerateCode(@AuthenticationPrincipal User user) {

		authService.reGenerateCode(user);
		return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}

	@Operation(summary = SwaggerMessages.FORGET_PASSWORD, description = SwaggerMessages.FORGET_PASSWORD_DESC)
	@PostMapping("/forget-password")
	public ResponseEntity<BasicResponse> forgetPassword(@Valid @RequestBody EmailRequestDTO email) {
		authService.forgetPassword(email);
		return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}

	@Operation(summary = SwaggerMessages.RESET_PASSWORD, description = SwaggerMessages.RESET_PASSWORD_DESC)
	@PostMapping("/reset-password")
	public ResponseEntity<BasicResponse> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswodDTO) {
		authService.resetPassword(resetPasswodDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}
}