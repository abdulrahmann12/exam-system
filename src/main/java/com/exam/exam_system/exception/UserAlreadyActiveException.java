package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserAlreadyActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyActiveException() {
		super(Messages.USER_ALREADY_ACTIVE);
	}
}
