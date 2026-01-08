package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentExamRowDTO {

    private Long studentId;
    private String studentName;

    private Integer score;
    private String status; // PASSED / FAILED / ABSENT
}

