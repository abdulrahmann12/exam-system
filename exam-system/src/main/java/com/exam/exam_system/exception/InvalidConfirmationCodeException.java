package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidConfirmationCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidConfirmationCodeException() {
		super(Messages.INVALID_CONFIRM_EMAIL);
	}
}