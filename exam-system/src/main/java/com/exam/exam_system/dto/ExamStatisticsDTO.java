package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamStatisticsDTO {

    private Long examId;

    private Long totalStudents;
    private Long submittedStudents;
    private Long absentStudents;

    private Double averageScore;
    private Double maxScore;
    private Double minScore;
}
