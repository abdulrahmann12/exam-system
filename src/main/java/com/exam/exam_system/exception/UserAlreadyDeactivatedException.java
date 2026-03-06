package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserAlreadyDeactivatedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UserAlreadyDeactivatedException() {
		super(Messages.USER_ALREADY_DEACTIVATE);
	}
	
}