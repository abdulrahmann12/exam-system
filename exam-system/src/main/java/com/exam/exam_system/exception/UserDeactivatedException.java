package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserDeactivatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserDeactivatedException() {
		super(Messages.USER_DEACTIVATED_EXCEPTION);
	}

}
