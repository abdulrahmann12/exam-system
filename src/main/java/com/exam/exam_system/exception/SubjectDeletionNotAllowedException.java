package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SubjectDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SubjectDeletionNotAllowedException() {
		super(Messages.CANNOT_DELETE_SUBJECT_WITH_EXAMS);
	}

}
