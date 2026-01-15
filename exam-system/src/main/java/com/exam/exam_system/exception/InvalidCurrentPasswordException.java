package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidCurrentPasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCurrentPasswordException() {
		super(Messages.INVALID_PASSWORD);
	}
}