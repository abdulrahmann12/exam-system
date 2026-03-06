package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class EmailAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super(Messages.EMAIL_ALREADY_EXISTS);
	}
	
}