package com.exam.exam_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChangePasswordRequestDTO {

	@NotBlank(message = "Current password is required")
	private String oldPassword;

	@NotBlank(message = "New password is required")
	@Size(min = 6, message = "New password must be at least 6 characters long")
	private String newPassword;
}
