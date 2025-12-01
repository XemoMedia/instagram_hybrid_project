package com.xmedia.social.youtube.model;

import java.time.OffsetDateTime;

import jakarta.persistence.CascadeType;
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
public class YouTubeComment {
    @Id
    private String id;
    private String kind;
    private String etag;
    private String channelId;
    private String videoId;
    private String textDisplay;
    private String textOriginal;
    private String authorDisplayName;
    private String authorProfileImageUrl;
    private String authorChannelUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_channel_id_value", referencedColumnName = "channel_value")
    private YouTubeAuthorChannelId authorChannelId;

    private Boolean canRate;
    private String viewerRating;
    private Integer likeCount;
    private OffsetDateTime publishedAt;
    private OffsetDateTime updatedAt;
}
