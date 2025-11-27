package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopLevelComment {
    private String kind;
    private String etag;
    private String id;
    private CommentSnippet snippet;
}
