package com.example.server.services;

import com.example.server.enums.TicketStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class EmailService {
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendMail(
            String to,
            String subject,
            String userName,
            Long ticketId,
            TicketStatus ticketStatus,
            String messageBody
    ) throws MessagingException, IOException {

        // Load the HTML template from resources
        ClassPathResource resource =
                new ClassPathResource("templates/ticket-update.html");

        String html = Files.readString(
                resource.getFile().toPath(),
                StandardCharsets.UTF_8
        );

        // Replace placeholders with dynamic values
        html = html.replace("{{userName}}", escapeHtml(userName))
                .replace("{{ticketId}}", escapeHtml(ticketId.toString()))
                .replace("{{ticketStatus}}",
                        escapeHtml(ticketStatus.name()))
                .replace("{{subject}}", escapeHtml(subject))
                .replace("{{messageBody}}",
                        escapeHtml(messageBody)
                                .replace("\r\n", "\n")
                                .replace("\n", "<br>"));

        // Construct the email
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                true,
                StandardCharsets.UTF_8.name()
        );

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("[" + ticketId + "] " + subject);
        helper.setText(html, true);

        // Send the email
        mailSender.send(mimeMessage);
    }


    public void sendOtpEmail(
            String to,
            String userName,
            String otp,
            int expiryMinutes
    ) throws MessagingException, IOException {

        // Load the HTML template from resources
        ClassPathResource resource =
                new ClassPathResource("templates/otp-email.html");

        // Read the template safely, including when packaged in a JAR
        String html = Files.readString(
                resource.getFile().toPath(),
                StandardCharsets.UTF_8
        );

        // Replace placeholders with dynamic values
        html = html.replace("{{userName}}", escapeHtml(userName))
                .replace("{{otp}}", escapeHtml(otp))
                .replace("{{expiryMinutes}}",
                        String.valueOf(expiryMinutes));

        // Construct the email
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                true,
                StandardCharsets.UTF_8.name()
        );

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("IT Help Desk - Email Verification Code");
        helper.setText(html, true);

        // Send the email
        mailSender.send(mimeMessage);
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
