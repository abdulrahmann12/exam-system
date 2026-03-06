package com.exam.exam_system.rabbitconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.exam.exam_system.rabbitconfig.RabbitConstants.*;

@Configuration
public class AuthRabbitConfig {

	@Bean
	public TopicExchange authExchange() {
	    return new TopicExchange(AUTH_EXCHANGE);
	}

	@Bean
	public Queue userChangeEmailQueue() {
	    return new Queue(EMAIL_CHANGE_QUEUE, true);
	}
	
	@Bean
	public Queue userRegisteredQueue() {
	    return new Queue(USER_REGISTERED_QUEUE, true);
	}

	@Bean
	public Queue passwordResetQueue() {
	    return new Queue(PASSWORD_RESET_QUEUE, true);
	}
	
	@Bean
	public Queue regenerateCodeQueue() {
	    return new Queue(CODE_REGENERATED_QUEUE, true);
	}

	@Bean
	public Binding userChangeEmailBinding(TopicExchange authExchange, Queue userChangeEmailQueue) {
	    return BindingBuilder.bind(userChangeEmailQueue).to(authExchange).with(EMAIL_CHANGE_KEY);
	}
	
	@Bean
	public Binding userRegisteredBinding(TopicExchange authExchange, Queue userRegisteredQueue) {
	    return BindingBuilder.bind(userRegisteredQueue).to(authExchange).with(USER_REGISTERED_KEY);
	}

	@Bean
	public Binding passwordResetBinding(TopicExchange authExchange, Queue passwordResetQueue) {
	    return BindingBuilder.bind(passwordResetQueue).to(authExchange).with(PASSWORD_RESET_KEY);
	}

	@Bean
	public Binding regenerateCodeBinding(TopicExchange authExchange, Queue regenerateCodeQueue) {
	    return BindingBuilder.bind(regenerateCodeQueue).to(authExchange).with(CODE_REGENERATED_KEY);
	}
}