package com.exam.exam_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Code is required")
	private String code;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password too short")
	private String newPassword;
}