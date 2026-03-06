package com.exam.exam_system.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChoiceAdminViewDTO {

	private Long choiceId;
	private String choiceText;
	private Integer choiceOrder;
	private Boolean isCorrect;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
