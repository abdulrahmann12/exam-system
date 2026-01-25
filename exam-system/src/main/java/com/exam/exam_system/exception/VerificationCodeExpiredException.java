package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class VerificationCodeExpiredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VerificationCodeExpiredException() {
		super(Messages.EXPIRED_CODE);
	}
}