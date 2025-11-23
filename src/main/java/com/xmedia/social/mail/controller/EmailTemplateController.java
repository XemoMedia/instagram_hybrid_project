package com.xmedia.social.mail.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.mail.entity.EmailTemplate;
import com.xmedia.social.mail.service.EmailTemplateService;

@RestController
@RequestMapping("/api/email/template")
public class EmailTemplateController {

    @Autowired
    private EmailTemplateService service;

    // CREATE
    @PostMapping
    public ResponseEntity<EmailTemplate> create(@RequestBody EmailTemplate template) {
        return ResponseEntity.ok(service.createTemplate(template));
    }

    // GET ONE
    @GetMapping("/{name}")
    public ResponseEntity<EmailTemplate> getByName(@PathVariable("name") String templateName) {
        return ResponseEntity.ok(service.getTemplateByName(templateName));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<EmailTemplate>> getAll() {
        return ResponseEntity.ok(service.getAllTemplates());
    }

    // UPDATE
    @PutMapping("/{name}")
    public ResponseEntity<EmailTemplate> update(
            @PathVariable("name") String templateName,
            @RequestBody EmailTemplate template) {

        return ResponseEntity.ok(service.updateTemplate(templateName, template));
    }

    // DELETE
    @DeleteMapping("/{name}")
    public ResponseEntity<String> delete(@PathVariable("name") String templateName) {
        service.deleteTemplate(templateName);
        return ResponseEntity.ok("Template deleted: " + templateName);
    }
}
