package com.exam.exam_system.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamQrResponseDTO {

    private Long examId;
    private String qrCodeUrl;
    private LocalDateTime expiresAt;
}
