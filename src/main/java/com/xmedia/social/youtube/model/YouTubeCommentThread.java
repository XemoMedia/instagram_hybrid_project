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
public class YouTubeCommentThread {
    @Id
    private String id;
    private String kind;
    private String etag;
    private String channelId;
    private String videoId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "top_level_comment_id", referencedColumnName = "id")
    private YouTubeComment topLevelComment;

    private Boolean canReply;
    private Integer totalReplyCount;
    private Boolean isPublic;
}
