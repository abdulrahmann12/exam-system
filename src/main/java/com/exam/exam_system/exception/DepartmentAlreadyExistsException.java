package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DepartmentAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DepartmentAlreadyExistsException() {
		super(Messages.DEPARTMENT_ALREADY_EXISTS);
	}
	
}