package com.xmedia.social.mail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "email_template")
public class EmailTemplate {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", length = 255, unique = true, nullable = false)
    private String templateName;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    public EmailTemplate() {}

    public EmailTemplate(String templateName, String subject, String body) {
        this.templateName = templateName;
        this.subject = subject;
        this.body = body;
    }

    // GETTERS & SETTERS
    // ...
}
