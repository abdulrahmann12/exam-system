package com.exam.exam_system.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentGetResponseDTO {

    private Long departmentId;
    private String departmentName;
    private Long collegeId;
    private String collegeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
