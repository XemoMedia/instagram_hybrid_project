package com.xmedia.social.youtube.entity;

import com.xmedia.social.youtube.model.YouTubeChannelSnippetEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "youtube_channel_item")
public class YouTubeChannelItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kind;
    private String etag;
    private String youtubeChannelId; // Renamed from 'id' to avoid conflict with entity ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_channel_dat-id", nullable = false)
    private YouTubeChannelDataEntity youTubeChannelData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_snippet_id", referencedColumnName = "id")
    private YouTubeChannelSnippetEntity snippet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_details_id", referencedColumnName = "id")
    private YouTubeContentDetailsEntity contentDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id", referencedColumnName = "id")
    private YouTubeStatistics statistics;
}
