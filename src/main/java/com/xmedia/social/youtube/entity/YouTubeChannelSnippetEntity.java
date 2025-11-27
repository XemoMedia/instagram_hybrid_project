package com.xmedia.social.youtube.entity;

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

import java.time.LocalDateTime;

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
    private LocalDateTime publishedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_details_id", referencedColumnName = "id")
    private YouTubeThumbnailDetails thumbnails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "localized_id", referencedColumnName = "id")
    private YouTubeLocalized localized;
}
