package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PermissionGetResponseDTO {

    private Long permissionId;
    private String code;
    private String description;
    private Boolean active;
}
