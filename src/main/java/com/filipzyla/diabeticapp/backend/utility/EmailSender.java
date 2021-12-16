package com.filipzyla.diabeticapp.backend.utility;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSender() {
        javaMailSender = new JavaMailSenderImpl();
    }

    public void sendEmail(String toEmail, String subject, String text, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }

}