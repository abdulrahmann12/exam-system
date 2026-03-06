package com.exam.exam_system.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.events.PasswordResetRequestedEvent;
import com.exam.exam_system.exception.MailSendingException;
import com.exam.exam_system.rabbitconfig.RabbitConstants;
import com.exam.exam_system.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetConsumer {
	private final EmailService emailService;

	@RabbitListener(queues = RabbitConstants.PASSWORD_RESET_QUEUE)
	public void handlePasswordResetRequested(PasswordResetRequestedEvent event) {
		System.out.println("📩 Received PasswordResetRequestedEvent for user: " + event.getEmail());

		try {
			emailService.sendCode(event, Messages.RESET_PASSWORD);
		} catch (Exception e) {
			throw new MailSendingException();
		}
	}
}
