package com.exam.exam_system.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateExamRequestDTO {

    @NotBlank(message = "Exam title is required")
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "College is required")
    private Long collegeId;

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Subject is required")
    private Long subjectId;

    @NotNull
    private Boolean isActive;
    
    @NotNull
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @NotNull
    @Min(value = 10, message = "Per question time must be at least 10 seconds")
    private Integer perQuestionTimeSeconds;

    private Boolean allowBackNavigation;

    private Boolean randomizeQuestions;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotEmpty(message = "Exam must contain at least one question")
    private List<@Valid UpdateQuestionRequestDTO> questions;
}
