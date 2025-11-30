package com.xmedia.social.youtube.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class YouTubeSearchSnippet {
    @Id
    private String id;
    private OffsetDateTime publishedAt;
    private String channelId;
    private String title;
    private String description;

    private String defaultThumbnailUrl;
    @OneToOne
    @JoinColumn(name = "defaultThumbnailUrl", referencedColumnName = "url", insertable = false, updatable = false)
    private YouTubeThumbnail defaultThumbnail;

    private String mediumThumbnailUrl;
    @OneToOne
    @JoinColumn(name = "mediumThumbnailUrl", referencedColumnName = "url", insertable = false, updatable = false)
    private YouTubeThumbnail mediumThumbnail;

    private String highThumbnailUrl;
    @OneToOne
    @JoinColumn(name = "highThumbnailUrl", referencedColumnName = "url", insertable = false, updatable = false)
    private YouTubeThumbnail highThumbnail;

    private String channelTitle;
    private String liveBroadcastContent;
    private OffsetDateTime publishTime;
}
