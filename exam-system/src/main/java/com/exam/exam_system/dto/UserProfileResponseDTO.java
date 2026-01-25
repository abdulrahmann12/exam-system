package com.exam.exam_system.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponseDTO {

	private Long userId;

	private String username;
	private String email;

	private String firstName;
	private String lastName;
	private String phone;

	private String roleName;
	private String collegeName;
	private String departmentName;

	private LocalDateTime createdAt;
}
