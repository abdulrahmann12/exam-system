package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ExamStudentViewDTO {

    private Long examId;

    private String title;
    private String description;

    private Integer durationMinutes;
    private Integer perQuestionTimeSeconds;

    private Boolean allowBackNavigation;
    private Boolean randomizeQuestions;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<QuestionStudentViewDTO> questions;
}
