package com.xmedia.social.youtube.model;

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
public class YouTubeChannel {
    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "snippet_id", referencedColumnName = "id")
    private YouTubeChannelSnippetEntity snippet;

    private String likesPlaylistId;
    private String uploadsPlaylistId;
    private Long viewCount;
    private Long subscriberCount;
    private Boolean hiddenSubscriberCount;
    private Long videoCount;
}
