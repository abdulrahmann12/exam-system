package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeCreateRequestDTO {
	
    @NotBlank(message = ValidationMessages.COLLEGE_NAME_REQUIRED)
    private String collegeName;
}