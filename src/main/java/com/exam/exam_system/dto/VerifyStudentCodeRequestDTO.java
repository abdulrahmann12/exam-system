package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyStudentCodeRequestDTO {

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotBlank(message = "Student code is required")
    private String studentCode;

    @NotBlank(message = "QR token is required")
    private String token;
}
