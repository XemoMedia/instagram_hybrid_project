package com.xmedia.social.mail.dto;

import lombok.Data;

@Data
public class EmailTemplateDto {
	private String templateName;
    private String subject;
    private String body;
    private String type;
}
