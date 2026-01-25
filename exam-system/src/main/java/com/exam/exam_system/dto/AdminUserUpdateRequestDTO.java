package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserUpdateRequestDTO {

    @NotNull(message = "Role is required")
    private Long roleId;

    @NotNull(message = "College is required")
    private Long collegeId;

    @NotNull(message = "Department is required")
    private Long departmentId;
    
	private Boolean isActive;
}
