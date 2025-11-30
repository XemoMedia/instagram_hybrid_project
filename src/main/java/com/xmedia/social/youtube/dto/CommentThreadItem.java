package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentThreadItem {
    private String kind;
    private String etag;
    private String id;
    private CommentThreadSnippet snippet;
}
