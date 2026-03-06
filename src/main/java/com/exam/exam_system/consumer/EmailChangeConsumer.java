package com.exam.exam_system.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.events.EmailChangeEvent;
import com.exam.exam_system.rabbitconfig.RabbitConstants;
import com.exam.exam_system.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailChangeConsumer {

	private final EmailService emailService;
	
	
	@RabbitListener(queues = RabbitConstants.EMAIL_CHANGE_QUEUE)
	public void handleCodeRegeneratedEvent(EmailChangeEvent event) {
		System.out.println("📩 Received CodeRegeneratedEvent for user: " + event.getNewEmail());

		try {
			emailService.sendCode(event, Messages.RESEND_CODE);
		} catch (Exception e) {
			throw new com.exam.exam_system.exception.MailSendingException();
		}
	}
	
}
