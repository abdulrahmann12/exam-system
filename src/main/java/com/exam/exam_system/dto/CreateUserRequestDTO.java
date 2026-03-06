package com.exam.exam_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(
        regexp = "^01[0-2,5]{1}[0-9]{8}$",
        message = "Invalid Egyptian phone number"
    )
    private String phone;

    @NotNull(message = "Role is required")
    private Long roleId;

    @NotNull(message = "College is required")
    private Long collegeId;

    @NotNull(message = "Department is required")
    private Long departmentId;
}
