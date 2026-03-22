package com.exam.exam_system.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateExamRequestDTO {

    @NotBlank(message = ValidationMessages.EXAM_TITLE_REQUIRED)
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
    private Long collegeId;

    @NotNull(message = ValidationMessages.DEPARTMENT_ID_REQUIRED)
    private Long departmentId;

    @NotNull(message = ValidationMessages.SUBJECT_ID_REQUIRED)
    private Long subjectId;

    @NotNull
    private Boolean isActive;
    
    @NotNull
    @Min(value = 1, message = ValidationMessages.EXAM_DURATION_MIN)
    private Integer durationMinutes;

    @NotNull
    @Min(value = 10, message = ValidationMessages.EXAM_PER_QUESTION_TIME_MIN_10)
    private Integer perQuestionTimeSeconds;

    private Boolean allowBackNavigation;

    private Boolean randomizeQuestions;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotEmpty(message = ValidationMessages.EXAM_QUESTIONS_REQUIRED)
    private List<@Valid UpdateQuestionRequestDTO> questions;
}