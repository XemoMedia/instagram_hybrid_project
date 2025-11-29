package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnails {
    @JsonProperty("default")
    private Thumbnail defaultThumbnail;
    private Thumbnail medium;
    private Thumbnail high;
}
