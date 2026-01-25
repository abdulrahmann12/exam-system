package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class TooManyRequestsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TooManyRequestsException() {
		super(Messages.TOO_MANY_REQUESTS);
	}
	
}