package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class CollegeNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CollegeNotFoundException() {
		super(Messages.COLLEGE_NOT_FOUND);
	}
}
