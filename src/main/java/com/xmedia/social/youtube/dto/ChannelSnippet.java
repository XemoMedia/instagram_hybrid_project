package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSnippet {
    private String title;
    private String description;
    private String customUrl;
    private LocalDateTime publishedAt;
    private ThumbnailDetails thumbnails;
    private Localized localized;
}
