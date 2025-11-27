package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentThreadItem {
    private String kind;
    private String etag;
    private String id;
    private CommentThreadSnippet snippet;
    private Replies replies;
}
