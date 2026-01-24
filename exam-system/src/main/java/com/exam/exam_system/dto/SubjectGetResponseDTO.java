package com.exam.exam_system.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectGetResponseDTO {

    private Long subjectId;
    private String subjectName;
    private Long departmentId;
    private String departmentName;
	private String subjectCode;
    private Long collegeId;
    private String collegeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
