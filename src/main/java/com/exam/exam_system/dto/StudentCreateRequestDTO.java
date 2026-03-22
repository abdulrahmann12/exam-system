package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCreateRequestDTO {

	@NotNull(message = ValidationMessages.USER_ID_REQUIRED)
    private Long userId;

    @NotBlank(message = ValidationMessages.STUDENT_CODE_REQUIRED)
    private String studentCode;

    @NotNull(message = ValidationMessages.ACADEMIC_YEAR_REQUIRED)
    private Integer academicYear;
}