package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class RoleNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RoleNotFoundException() {
		super(Messages.ROLE_NOT_FOUND);
	}
}
