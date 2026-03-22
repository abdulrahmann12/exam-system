package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = ValidationMessages.USERNAME_REQUIRED)
    @Size(min = 3, max = 30, message = ValidationMessages.USERNAME_SIZE)
    private String username;

    @NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
    @Email(message = ValidationMessages.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    @Size(min = 8, message = ValidationMessages.PASSWORD_SIZE)
    private String password;

    @NotBlank(message = ValidationMessages.FIRST_NAME_REQUIRED)
    private String firstName;

    @NotBlank(message = ValidationMessages.LAST_NAME_REQUIRED)
    private String lastName;

    @Pattern(
        regexp = "^01[0-2,5]{1}[0-9]{8}$",
        message = ValidationMessages.PHONE_INVALID
    )
    private String phone;

    @NotNull(message = ValidationMessages.ROLE_ID_REQUIRED)
    private Long roleId;

    @NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
    private Long collegeId;

    @NotNull(message = ValidationMessages.DEPARTMENT_ID_REQUIRED)
    private Long departmentId;
}