package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSnippet {
    private String publishedAt;
    private String channelId;
    private String title;
    private String description;
    private ThumbnailDetails thumbnails;
    private String channelTitle;
    private String liveBroadcastContent;
    private String publishTime;
}
