package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamResultDTO {

    private Long examId;
    private String examTitle;

    private Integer totalMarks;
    private Integer obtainedMarks;

    private String grade;
    private Boolean passed;
}
