package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DepartmentNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DepartmentNotFoundException() {
		super(Messages.DEPARTMENT_NOT_FOUND);
	}
}
