package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SubjectAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SubjectAlreadyExistsException() {
		super(Messages.SUBJECT_ALREADY_EXISTS);
	}
	
}