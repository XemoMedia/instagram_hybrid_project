package com.xmedia.social.youtube.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet {
    private String title;
    private String description;
    private String customUrl;
    private OffsetDateTime publishedAt;
    private Thumbnails thumbnails;
    private Localized localized;
}
