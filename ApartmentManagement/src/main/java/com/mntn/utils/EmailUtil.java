package com.mntn.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "imagicloud2@gmail.com";
    private static final String PASSWORD = "loxi pucc fqca vdpq";

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // 1. Cấu hình properties cho SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // sử dụng TLS
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        // 2. Tạo phiên và authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        session.setDebug(true);

        // 3. Tạo message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
        );
        message.setSubject(subject);
        message.setText(body);

        // 4. Gửi email
        Transport.send(message);
    }


    public static void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        // Thiết lập nội dung HTML
        message.setContent(htmlContent, "text/html; charset=UTF-8");

        Transport.send(message);
    }
}

