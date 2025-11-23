package com.xmedia.social.mail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmedia.social.mail.entity.EmailConfig;
import com.xmedia.social.mail.repository.EmailConfigRepository;

@Service
public class EmailConfigService {

	@Autowired
    private EmailConfigRepository repo;

   // CREATE
    public EmailConfig create(EmailConfig config) {
        return repo.save(config);
    }

    // READ BY HOST
    public EmailConfig getByHost(String host) {
        return repo.findByHost(host)
                .orElseThrow(() -> new RuntimeException("EmailConfig not found for host: " + host));
    }

    // UPDATE BY HOST
    public EmailConfig updateByHost(String host, EmailConfig newData) {

        EmailConfig existing = getByHost(host);

        existing.setHost(newData.getHost());
        existing.setPort(newData.getPort());
        existing.setUsername(newData.getUsername());
        existing.setPassword(newData.getPassword());
        existing.setSmtpAuth(newData.getSmtpAuth());
        existing.setStarttlsEnable(newData.getStarttlsEnable());

        return repo.save(existing);
    }

    // DELETE BY HOST
    public void deleteByHost(String host) {
        if (!repo.existsByHost(host)) {
            throw new RuntimeException("EmailConfig not found for host: " + host);
        }
        repo.deleteByHost(host);
    }

    // READ ALL
    public List<EmailConfig> getAll() {
        return repo.findAll();
    }
}
