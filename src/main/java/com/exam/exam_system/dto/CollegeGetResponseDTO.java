package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CollegeGetResponseDTO {

    private Long collegeId;
    private String collegeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}