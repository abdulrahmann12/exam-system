package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ExamSummaryDTO {

    private Long examId;
    private String title;

    private String subjectName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private Integer totalQuestions;

    private Boolean isActive;
}
