package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class RoleGetResponseDTO {

    private Long roleId;
    private String roleName;
    private Set<PermissionGetResponseDTO> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}