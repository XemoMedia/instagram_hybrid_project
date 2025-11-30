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
@Table(name = "youtube_comment_thread_snippet")
public class YouTubeCommentThreadSnippetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String channelId;
    private String videoId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "top_level_comment_id", referencedColumnName = "id")
    private YouTubeTopLevelComment topLevelComment;
    private Boolean canReply;
    private Integer totalReplyCount;
    private Boolean isPublic;
}
