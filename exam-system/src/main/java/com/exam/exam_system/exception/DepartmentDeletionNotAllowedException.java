package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DepartmentDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DepartmentDeletionNotAllowedException() {
		super(Messages.CANNOT_DELETE_DEPARTMENT_SUBJECS);
	}

}
