package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class MailSendingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MailSendingException() {
		super(Messages.FAILED_EMAIL);
	}

}
