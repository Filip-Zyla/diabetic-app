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

    public void registerEmail(User user) {
        String subject = "Confirm registration";
        String text = "Your account has been created.\n" +
                "Credentials:\n" +
                "login - %s\n" +
                "password - %s\n" +
                "You can log in.";
        try {
            sendEmail(user.getEmail(), subject, String.format(text, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(User user) {
        String subject = "Forgotten credentials";
        String text = "Your credentials:\n" +
                "login - %s\n" +
                "password - %s\n";
        try {
            sendEmail(user.getEmail(), subject, String.format(text, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void changeCredentials(User user) {
        String subject = "New credentials";
        String text = "Your credentials:\n" +
                "login - %s\n" +
                "password - %s\n" +
                "email - %s\n";
        try {
            sendEmail(user.getEmail(), subject, String.format(text, user.getUsername(), user.getPassword(), user.getEmail()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}