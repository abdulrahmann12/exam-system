package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class PhoneAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PhoneAlreadyExistsException() {
		super(Messages.PHONE_ALREADY_EXISTS);
	}
	
}