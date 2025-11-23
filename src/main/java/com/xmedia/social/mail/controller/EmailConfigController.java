package com.xmedia.social.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.mail.entity.EmailConfig;
import com.xmedia.social.mail.service.EmailConfigService;

@RestController
@RequestMapping("/api/email-config")
public class EmailConfigController {

	@Autowired
    private EmailConfigService service;
	
	// CREATE NEW EMAIL CONFIG (POST)
    @PostMapping
    public ResponseEntity<EmailConfig> createEmailConfig(@RequestBody EmailConfig config) {
        EmailConfig savedConfig = service.create(config);
        return ResponseEntity.ok(savedConfig);
    }

    // GET BY HOST
    @GetMapping("/get")
    public ResponseEntity<EmailConfig> getConfig(@RequestParam String host) {
        return ResponseEntity.ok(service.getByHost(host));
    }


    // UPDATE BY HOST
    @PutMapping("/host/{host}")
    public EmailConfig updateByHost(@PathVariable String host, @RequestBody EmailConfig config) {
        return service.updateByHost(host, config);
    }

    // DELETE BY HOST
    @DeleteMapping("/host/{host}")
    public String deleteByHost(@PathVariable String host) {
        service.deleteByHost(host);
        return "Deleted config for host: " + host;
    }

}

