package com.xmedia.social.youtube.entity;

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
@Table(name = "youtube_comment_thread_item")
public class YouTubeCommentThreadItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kind;
    private String etag;
    private String youtubeCommentThreadItemId; // Renamed from 'id' to avoid conflict with entity ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_comment_thread_dat-id", nullable = false)
    private YouTubeCommentThreadDataEntity youTubeCommentThreadData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_thread_snippet_id", referencedColumnName = "id")
    private YouTubeCommentThreadSnippetEntity snippet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "replies_id", referencedColumnName = "id")
    private YouTubeReplies replies;
}
