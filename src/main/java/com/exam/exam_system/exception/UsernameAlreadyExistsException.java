package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UsernameAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameAlreadyExistsException() {
		super(Messages.USERNAME_ALREADY_EXISTS);
	}

}