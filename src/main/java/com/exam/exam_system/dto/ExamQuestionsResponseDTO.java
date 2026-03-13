package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamQuestionsResponseDTO {

    private Long sessionId;
    private Long examId;
    private String examTitle;
    private Integer durationMinutes;
    private Integer perQuestionTimeSeconds;
    private Boolean allowBackNavigation;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private Integer totalQuestions;
    private List<QuestionStudentViewDTO> questions;
}
