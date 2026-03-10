package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentUpdateProfileRequestDTO {

	@NotBlank(message = "Academic code is required")
	private String studentCode;

	@NotNull(message = "Academic year is required")
	private Integer academicYear;
}