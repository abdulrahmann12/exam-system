package com.exam.exam_system.dto;

import java.time.LocalDateTime;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateExamWithFileRequestDTO {

	@NotBlank(message = ValidationMessages.EXAM_TITLE_REQUIRED)
	@Size(min = 3, max = 100)
	private String title;

	@Size(max = 1000)
	@NotBlank(message = ValidationMessages.EXAM_DESCRIPTION_REQUIRED)
	private String description;

	@NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
	private Long collegeId;

	@NotNull(message = ValidationMessages.DEPARTMENT_ID_REQUIRED)
	private Long departmentId;

	@NotNull(message = ValidationMessages.SUBJECT_ID_REQUIRED)
	private Long subjectId;

	@NotNull
	@Min(value = 1, message = ValidationMessages.EXAM_DURATION_MIN)
	private Integer durationMinutes;

	@NotNull
	@Min(value = 30, message = ValidationMessages.EXAM_PER_QUESTION_TIME_MIN_30)
	private Integer perQuestionTimeSeconds;

	@NotNull
	private Boolean allowBackNavigation;

	@NotNull
	private Boolean randomizeQuestions;

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

	@NotNull
	private Boolean isActive;
}