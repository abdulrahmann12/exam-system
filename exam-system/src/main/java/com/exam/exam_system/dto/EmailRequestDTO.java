package com.exam.exam_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
}