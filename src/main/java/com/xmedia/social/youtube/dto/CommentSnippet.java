package com.xmedia.social.youtube.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentSnippet {
    private String channelId;
    private String videoId;
    private String textDisplay;
    private String textOriginal;
    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String authorChannelUrl;
    private AuthorChannelId authorChannelId;
    private Boolean canRate;
    private String viewerRating;
    private Integer likeCount;
    private OffsetDateTime publishedAt;
    private OffsetDateTime updatedAt;
}
