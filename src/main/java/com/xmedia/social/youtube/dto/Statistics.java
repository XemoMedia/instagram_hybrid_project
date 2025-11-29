package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statistics {
    private String viewCount;
    private String subscriberCount;
    private Boolean hiddenSubscriberCount;
    private String videoCount;
}
