package com.mycompany.myapp.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String prepareHtmlContent(String uniqueLink, String username) {
        String htmlContent =
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "body {font-family: Arial, sans-serif; margin: 20px; padding: 20px; background-color: #f4f4f4; color: #333;}" +
            ".container {background-color: #fff; border: 1px solid #ddd; padding: 20px; border-radius: 5px;}" +
            "h1 {color: #0066cc;}" +
            "a.button {display: inline-block; background-color: #0066cc; color: #ffffff; padding: 10px 15px; border-radius: 5px; text-decoration: none; font-weight: bold;}" +
            "p {line-height: 1.6;}" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class=\"container\">" +
            "<h1>Welcome to EvoCode, " +
            username +
            "!</h1>" +
            "<p>EvoCode is a world where magic and computer science are combined into one place. Our main source of knowledge is the <strong>Codex</strong> – a source of infinite knowledge that has spread across this world in more pieces. You are a young student from Evoland, and you've been chosen to reassemble the Codex and restore knowledge to the land.</p>" +
            "<p>With each fragment, you will not only rebuild the Codex but also unlock the secrets of programming, which is the essence of all creation in Evoland. You will also see the character evolve in a unique way.</p>" +
            "<p>Let's start with the first chapter.</p>" +
            "<a href=\"" +
            uniqueLink +
            "\" class=\"button\">Start Your Journey</a>" +
            "</div>" +
            "</body>" +
            "</html>";
        return htmlContent;
    }

    public void sendUniqueLinkEmail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("mikestrike555@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
