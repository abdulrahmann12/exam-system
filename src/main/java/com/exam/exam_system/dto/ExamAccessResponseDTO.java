package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamAccessResponseDTO {

    private Long examId;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private Boolean allowBackNavigation;
    private Boolean randomizeQuestions;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isCurrentlyAvailable;
}
