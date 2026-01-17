package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class CollegeAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CollegeAlreadyExistsException() {
		super(Messages.COLLEGE_ALREADY_EXISTS);
	}
	
}