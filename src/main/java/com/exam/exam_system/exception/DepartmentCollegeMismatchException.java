package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DepartmentCollegeMismatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DepartmentCollegeMismatchException() {
		super(Messages.DEPARTMENT_NOT_BELONG_TO_COLLEGE);
	}

}
