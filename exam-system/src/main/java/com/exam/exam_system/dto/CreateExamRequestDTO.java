package com.exam.exam_system.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateExamRequestDTO {

	@NotBlank(message = "Exam title is required")
	@Size(min = 3, max = 100)
	private String title;

	@Size(max = 1000)
	@NotBlank(message = "Exam description is required")
	private String description;

	@NotNull(message = "College is required")
	private Long collegeId;

	@NotNull(message = "Department is required")
	private Long departmentId;

	@NotNull(message = "Subject is required")
	private Long subjectId;

	@NotNull
	@Min(value = 1, message = "Duration must be at least 1 minute")
	private Integer durationMinutes;

	@NotNull
	@Min(value = 30, message = "Per question time must be at least 30 seconds")
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

	@NotEmpty(message = "Exam must contain at least one question")
	private List<@Valid CreateQuestionRequestDTO> questions;
}
