package com.exam.exam_system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResultResponseDTO {

    private Long resultId;
    private Long sessionId;

    private Integer totalMark;
    private Integer maxMark;

    private LocalDateTime submittedAt;
}
