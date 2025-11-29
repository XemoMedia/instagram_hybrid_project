package com.xmedia.social.youtube.model;

import java.time.OffsetDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "youtube_channel_snippet")
public class YouTubeChannelSnippetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String customUrl;
    private OffsetDateTime publishedAt;

    @OneToOne
    @JoinColumn(name = "default_thumbnail_url", referencedColumnName = "url")
    private YouTubeThumbnail defaultThumbnail;

    @OneToOne
    @JoinColumn(name = "medium_thumbnail_url", referencedColumnName = "url")
    private YouTubeThumbnail mediumThumbnail;

    @OneToOne
    @JoinColumn(name = "high_thumbnail_url", referencedColumnName = "url")
    private YouTubeThumbnail highThumbnail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "localized_id", referencedColumnName = "id")
    private YouTubeLocalized localized;
}
