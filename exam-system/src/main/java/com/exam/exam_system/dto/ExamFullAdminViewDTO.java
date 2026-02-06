package com.exam.exam_system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamFullAdminViewDTO {

	private Long examId;

	private String title;
	private String description;

	private Long collegeId;
	private String collegeName;

	private Long departmentId;
	private String departmentName;

	private Long subjectId;
	private String subjectName;
	
	private String qrCodeUrl;
	private String qrToken;
	private LocalDateTime qrExpiresAt;
	
	private Long createdById;
	private String createdByName;

	private Integer durationMinutes;
	private Integer perQuestionTimeSeconds;
	private Boolean allowBackNavigation;
	private Boolean randomizeQuestions;

	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private Integer totalQuestions;
	private Boolean isActive;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

    private List<QuestionAdminViewDTO> questions;

}
