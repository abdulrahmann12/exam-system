package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class RoleDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleDeletionNotAllowedException() {
		super(Messages.CANNOT_DELETE_ROLE);
	}

}
