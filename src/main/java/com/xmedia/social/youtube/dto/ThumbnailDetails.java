package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailDetails {
    @JsonProperty("default")
    private Thumbnail defaultThumbnail;
    private Thumbnail medium;
    private Thumbnail high;
}
