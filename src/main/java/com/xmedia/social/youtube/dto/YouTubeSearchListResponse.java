package com.xmedia.social.youtube.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeSearchListResponse {
    private String kind;
    private String etag;
    private String regionCode;
    private PageInfo pageInfo;
    private List<SearchResultItem> items;
}
