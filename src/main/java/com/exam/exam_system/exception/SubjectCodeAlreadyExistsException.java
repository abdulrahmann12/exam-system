package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SubjectCodeAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SubjectCodeAlreadyExistsException() {
		super(Messages.SUBJECT_CODE_ALREADY_EXISTS);
	}
}
