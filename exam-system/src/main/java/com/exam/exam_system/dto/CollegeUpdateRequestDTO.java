package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
public class CollegeUpdateRequestDTO {
	
    @NotBlank(message = "College name is required")
    private String collegeName;
}
