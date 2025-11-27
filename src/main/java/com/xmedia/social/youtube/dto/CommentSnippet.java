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
public class CommentSnippet {
    private String channelId;
    private String videoId;
    private String textDisplay;
    private String textOriginal;
    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String authorChannelUrl;
    private AuthorChannelId authorChannelId;
    private boolean canRate;
    private String viewerRating;
    private int likeCount;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
}
