package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartExamRequestDTO {

    @NotNull(message = ValidationMessages.EXAM_ID_REQUIRED)
    private Long examId;

    @NotBlank(message = ValidationMessages.QR_TOKEN_REQUIRED)
    private String token;

    @NotBlank(message = ValidationMessages.STUDENT_CODE_REQUIRED)
    private String studentCode;
}