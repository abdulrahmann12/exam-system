package com.exam.exam_system.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.rabbitconfig.RabbitConstants;
import com.exam.exam_system.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeRegeneratedConsumer {

	private final EmailService emailService;
	
	
	@RabbitListener(queues = RabbitConstants.CODE_REGENERATED_QUEUE)
	public void handleCodeRegeneratedEvent(com.exam.exam_system.events.CodeRegeneratedEvent event) {
		System.out.println("📩 Received CodeRegeneratedEvent for user: " + event.getEmail());

		try {
			emailService.sendRegenerateCode(event, Messages.RESEND_CODE);
		} catch (Exception e) {
			throw new com.exam.exam_system.exception.MailSendingException();
		}
	}
	
}
