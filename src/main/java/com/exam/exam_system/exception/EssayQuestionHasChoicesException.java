package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class EssayQuestionHasChoicesException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EssayQuestionHasChoicesException() {
        super(Messages.ESSAY_QUESTION_HAS_CHOICES);
    }
}
