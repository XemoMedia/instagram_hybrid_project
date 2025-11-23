package com.xmedia.social.utility;

import java.util.Properties;

import org.springframework.stereotype.Service;

import com.xmedia.social.mail.entity.EmailConfig;
import com.xmedia.social.mail.entity.EmailTemplate;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtility {

	 public void sendEmail(EmailTemplate emailTemplate, EmailConfig config) {

	        Properties props = new Properties();
	        props.put("mail.smtp.auth", config.getSmtpAuth());
	        props.put("mail.smtp.starttls.enable", config.getStarttlsEnable());
	        props.put("mail.smtp.host", config.getHost());
	        props.put("mail.smtp.port", config.getPort());

	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(config.getUsername(), config.getPassword());
	            }
	        });

	        try {
	            MimeMessage message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(config.getUsername()));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getTo()));
	            message.setSubject(emailTemplate.getSubject());
	            message.setContent(emailTemplate.getBody(), "text/html; charset=utf-8");

	            Transport.send(message);

	            System.out.println("Mail sent successfully!");

	        } catch (Exception e) {
	            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
	        }
	    }
}
