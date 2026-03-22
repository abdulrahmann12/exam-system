package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import lombok.AllArgsConstructor;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleUpdateRequestDTO {
	@NotBlank(message = ValidationMessages.ROLE_NAME_REQUIRED)
	@Size(min = 3, max = 50, message = ValidationMessages.ROLE_NAME_SIZE)
	private String roleName;
	
	private List<Long> permissionIds;
}