package com.xmedia.social.schedular.dispatcher;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class PostContext {
	private String accessToken;
    private String title;
    private String description;
    private MultipartFile file;
}
