package com.exam.exam_system.dto;

import lombok.Data;

@Data
public class StudentGetResponseDTO {

	private Long studentId;

	private String studentCode;

	private Integer academicYear;

	private String firstName;
	private String lastName;
	private String email;
}