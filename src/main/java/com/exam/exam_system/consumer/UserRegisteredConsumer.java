package com.exam.exam_system.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.exam.exam_system.events.UserRegisteredEvent;
import com.exam.exam_system.exception.MailSendingException;
import com.exam.exam_system.rabbitconfig.RabbitConstants;
import com.exam.exam_system.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegisteredConsumer {
	private final EmailService emailService;
	
	@RabbitListener(queues = RabbitConstants.USER_REGISTERED_QUEUE)
	public void handleUserRegistered(UserRegisteredEvent event) {
       System.out.println("📩 Received UserRegisteredEvent for user: " + event.getEmail());

       try {
			emailService.sendWelcomeEmail(event);
		} catch (Exception e) {
			throw new MailSendingException();
		}
	}
}