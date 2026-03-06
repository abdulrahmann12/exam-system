package com.exam.exam_system.exception;


public class CollegeDeletionNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CollegeDeletionNotAllowedException(String message) {
		super(message);
	}

}
