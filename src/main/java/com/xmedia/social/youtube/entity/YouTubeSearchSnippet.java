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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "youtube_search_snippet")
public class YouTubeSearchSnippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String publishedAt;
    private String channelId;
    private String title;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_details_id", referencedColumnName = "id")
    private YouTubeThumbnailDetails thumbnails;
    private String channelTitle;
    private String liveBroadcastContent;
    private String publishTime;
}
