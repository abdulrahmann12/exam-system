package com.exam.exam_system.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDTO {

    private Long userId;

    private String username;
    private String firstName;
    private String lastName;

    private String roleName;

    private String collegeName;

    private String departmentName;
}
