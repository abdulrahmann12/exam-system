package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class RoleAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RoleAlreadyExistsException() {
		super(Messages.ROLE_ALREADY_EXISTS);
	}
	
}