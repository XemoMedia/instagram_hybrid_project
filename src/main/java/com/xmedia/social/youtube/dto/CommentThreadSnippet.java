package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentThreadSnippet {
    private String channelId;
    private String videoId;
    private TopLevelComment topLevelComment;
    private Boolean canReply;
    private Integer totalReplyCount;
    private Boolean isPublic;
}
