package com.exam.exam_system.dto;

import lombok.Data;

@Data
public class StudentProfileResponseDTO {

    private Long studentId;
    private String studentCode;
    private Integer academicYear;
    private Boolean isActive;

    // User info
    private UserProfileResponseDTO user;
}