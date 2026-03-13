package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO representing a single exam session from the student's perspective.
 * Used for listing "my exams" — shows session status and basic exam info.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSessionOverviewDTO {

    private Long sessionId;
    private Long examId;

    // Exam info
    private String examTitle;
    private String examDescription;
    private String subjectName;
    private Integer durationMinutes;
    private Integer totalQuestions;

    // Session info
    private Boolean isActive;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime expiresAt;

    // Result info (null if not yet submitted)
    private Integer totalMark;
    private Integer maxMark;

    // Computed status: IN_PROGRESS, COMPLETED, EXPIRED
    private String status;
}
