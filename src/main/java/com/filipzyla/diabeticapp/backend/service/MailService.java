package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String toEmail, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, false);
        javaMailSender.send(mimeMessage);
    }

    private final String LIST_CREDENTIALS = "Your credentials:\n login - %s\n password - %s\n";

    public void registerEmail(User user) {
        String subject = "Confirm registration";
        String text = "Account has been created.\n" + LIST_CREDENTIALS;
        try {
            sendEmail(user.getEmail(), subject, String.format(text, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(User user) {
        String subject = "New credentials";
        try {
            sendEmail(user.getEmail(), subject, String.format(LIST_CREDENTIALS, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void changeCredentials(User user) {
        String subject = "You changed credentials";
        try {
            sendEmail(user.getEmail(), subject, String.format(LIST_CREDENTIALS, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void changeEmail(User user) {
        String subject = "Changed email";
        try {
            sendEmail(user.getEmail(), subject, "Now this is email for your account on DiabApp");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}