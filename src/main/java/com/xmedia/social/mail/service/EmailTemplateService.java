package com.xmedia.social.mail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmedia.social.mail.entity.EmailTemplate;
import com.xmedia.social.mail.repository.EmailTemplateRepository;

@Service
public class EmailTemplateService {
	@Autowired
	private EmailTemplateRepository repo;

	public EmailTemplate createTemplate(EmailTemplate template) {
		return repo.save(template);
	}

	public EmailTemplate updateTemplate(String templateName, EmailTemplate template) {
		EmailTemplate existing = repo.findByTemplateName(templateName);

		if (existing == null) {
			throw new RuntimeException("Template not found: " + templateName);
		}

		existing.setSubject(template.getSubject());
		existing.setBody(template.getBody());

		return repo.save(existing);
	}

	public EmailTemplate getTemplateByName(String templateName) {
		return repo.findByTemplateName(templateName);
	}

	public List<EmailTemplate> getAllTemplates() {
		return repo.findAll();
	}

	public void deleteTemplate(String templateName) {
		EmailTemplate template = repo.findByTemplateName(templateName);

		if (template == null) {
			throw new RuntimeException("Template not found: " + templateName);
		}

		repo.delete(template);
	}
}
