package com.exam.exam_system.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {

	private String message;
	private Object data;
	private Date timestamp = new Date();

	public BasicResponse(String message) {
		this.message = message;
	}

	public BasicResponse(String message, Object data) {
		this.message = message;
		this.data = data;
	}
}