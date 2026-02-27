package com.exam.exam_system.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentExamSessionResponseDTO {

    private Long sessionId;
    private Long examId;
    private Long studentId;
    private String sessionCode;
    private Boolean isActive;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private List<StudentAnswerResponseDTO> answers;
}
