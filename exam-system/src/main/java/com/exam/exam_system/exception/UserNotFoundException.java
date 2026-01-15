package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super(Messages.USER_NOT_FOUND);
	}

}
