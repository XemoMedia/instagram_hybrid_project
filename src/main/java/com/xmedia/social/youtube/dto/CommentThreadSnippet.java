package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentThreadSnippet {
    private String channelId;
    private String videoId;
    private TopLevelComment topLevelComment;
    private boolean canReply;
    private int totalReplyCount;
    private boolean isPublic;
}
