package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessExamViaQrRequestDTO {

    @NotBlank(message = "QR token is required")
    private String token;
}
