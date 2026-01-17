package com.exam.exam_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeCreateRequestDTO {
	
    @NotBlank(message = "College name is required")
    private String collegeName;
}
