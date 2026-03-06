package com.qard.QardHasanah.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationLink = appBaseUrl + "/api/users/verify-email?token=" + token;

        // Keep local learning flow usable even if SMTP credentials are placeholders.
        if (fromEmail == null || fromEmail.isBlank() || fromEmail.contains("your-email")) {
            log.warn("Email is not configured. Verification link for {}: {}", toEmail, verificationLink);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Verify your email - Qard Hasanah");
        message.setText("Welcome! Please verify your email by clicking this link:\n\n" + verificationLink);

        mailSender.send(message);
    }
}

