package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SubjectNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SubjectNotFoundException() {
		super(Messages.SUBJECT_NOT_FOUND);
	}
}
