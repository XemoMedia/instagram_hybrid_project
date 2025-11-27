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
@Table(name = "youtube_comment_snippet")
public class YouTubeCommentSnippetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String channelId;
    private String videoId;
    private String textDisplay;
    private String textOriginal;
    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String authorChannelUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_channel_id", referencedColumnName = "id")
    private YouTubeAuthorChannelIdEntity authorChannelId;
    private Boolean canRate;
    private String viewerRating;
    private Integer likeCount;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
}
