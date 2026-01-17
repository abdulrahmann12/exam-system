package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoleCreateRequestDTO {

	@NotBlank(message = "Role name is required")
	@Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
	private String roleName;
}
