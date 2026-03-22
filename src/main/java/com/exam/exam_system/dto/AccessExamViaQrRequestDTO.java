package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessExamViaQrRequestDTO {

    @NotBlank(message = ValidationMessages.QR_TOKEN_REQUIRED)
    private String token;
}