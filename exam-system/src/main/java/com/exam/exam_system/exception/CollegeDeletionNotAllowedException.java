package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class CollegeDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CollegeDeletionNotAllowedException() {
		super(Messages.CANNOT_DELETE_COLLEGE);
	}

}
