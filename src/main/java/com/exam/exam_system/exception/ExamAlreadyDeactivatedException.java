package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamAlreadyDeactivatedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ExamAlreadyDeactivatedException() {
		super(Messages.EXAM_ALREADY_DEACTIVATE);
	}
	
}