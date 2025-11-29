package com.xmedia.social.youtube.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeCommentThreadListResponse {
    private String kind;
    private String etag;
    private PageInfo pageInfo;
    private List<CommentThreadItem> items;
}
