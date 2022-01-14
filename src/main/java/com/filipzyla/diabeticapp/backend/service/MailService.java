package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ResourceBundle;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    public ResourceBundle langResources;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        langResources = ResourceBundle.getBundle("lang.res");
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
        refresh();
        String subject = langResources.getString("new_acc");
        sendCredentials(user, subject);
    }

    public void forgotPassword(User user) {
        refresh();
        String subject = langResources.getString("new_cred");
        sendCredentials(user, subject);
    }

    public void changeCredentials(User user) {
        refresh();
        String subject = langResources.getString("change_cred");
        sendCredentials(user, subject);
    }

    private void sendCredentials(User user, String subject) {
        try {
            String listCredentials = langResources.getString("list_cred");
            sendEmail(user.getEmail(), subject, String.format(listCredentials, user.getUsername(), user.getPassword()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void changeEmail(User user) {
        refresh();
        String subject = langResources.getString("change_email");
        String text = langResources.getString("change_email_text");
        try {
            sendEmail(user.getEmail(), subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        langResources = ResourceBundle.getBundle("lang.res");
    }
}