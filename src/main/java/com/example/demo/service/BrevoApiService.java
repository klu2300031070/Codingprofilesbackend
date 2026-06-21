package com.example.demo.service;

import com.example.demo.dto.UserAnalysisReport;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class BrevoApiService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String smtpLoginUser;

    public void sendWelcomeEmailViaApi(String toEmail, String handle, UserAnalysisReport reportData) throws UnsupportedEncodingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            
            // Set multipart to true to support rich graphical CSS/HTML layouts
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject("CodeAnatic Real-time DSA Profile Analytics & Recommendations");
            
            // Sender display name fallback mapping
            helper.setFrom("rockykameshaissee808@gmail.com", "CodeAnatic Insights Engine");

            // 1. Map data properties directly to Thymeleaf context keys
            Context context = new Context();
            context.setVariable("handle", handle);
            context.setVariable("report", reportData);
            context.setVariable("username", handle);
            // 2. Compile layout engine markers matching templates/welcome-email.html
            String htmlContent = templateEngine.process("welcome-email", context);
            
            // Set text parameter with true flag enabling HTML rendering support
            helper.setText(htmlContent, true);

            // 3. Dispatch data payload through secure SMTP connection port relay
            mailSender.send(mimeMessage);
            System.out.println("Real-time visual dashboard email successfully dispatched to inbox: " + toEmail);
            
        } catch (MessagingException e) {
            throw new RuntimeException("CRITICAL: Failed to construct or dispatch code analytics email report", e);
        }
    }
}