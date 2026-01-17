package com.exam.exam_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.exam.exam_system.events.CodeRegeneratedEvent;
import com.exam.exam_system.events.PasswordResetRequestedEvent;
import com.exam.exam_system.events.UserRegisteredEvent;


@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	
	
    @Value("${spring.mail.username}")
    private String fromEmail;
   
   
    public void sendWelcomeEmail(UserRegisteredEvent user) {
        sendEmail(
            user.getEmail(),
            "Welcome to Exam System!",
            "emails/welcome", 
            new ContextBuilder()
                .add("username", user.getName()) 
                .build()
        );
    }
	
    public void sendCode(PasswordResetRequestedEvent user, String subject) {
        sendEmail(
                user.getEmail(),
                subject,
                "emails/send-code", // template path inside /resources/templates
                new ContextBuilder()
                        .add("name", user.getUsername())
                        .add("code", user.getCode())
                        .build()
        );
    }
    
    
    public void sendRegenerateCode(CodeRegeneratedEvent user, String subject) {
        sendEmail(
                user.getEmail(),
                subject,
                "emails/send-code", // template path inside /resources/templates
                new ContextBuilder()
                        .add("name", user.getUsername())
                        .add("code", user.getCode())
                        .build()
        );
    }
	
	
    private void sendEmail(String to, String subject, String templatePath, Context context) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templatePath, context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); 
            throw new RuntimeException("Failed to send email", e);
        }
    }
//    
//    private void sendEmailWithAttachment(
//            String to,
//            String subject,
//            String templatePath,
//            Context context,
//            byte[] attachmentBytes,
//            String attachmentFilename
//    ) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            String htmlContent = templateEngine.process(templatePath, context);
//            helper.setText(htmlContent, true);
//
//            // Attach the PDF
//            ByteArrayResource pdfAttachment = new ByteArrayResource(attachmentBytes);
//            helper.addAttachment(attachmentFilename, pdfAttachment);
//
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to send email with attachment", e);
//        }
//    }
//    
    private static class ContextBuilder {
        private final Context context = new Context();

        public ContextBuilder add(String key, Object value) {
            context.setVariable(key, value);
            return this;
        }

        public Context build() {
            return context;
        }
    }
}
