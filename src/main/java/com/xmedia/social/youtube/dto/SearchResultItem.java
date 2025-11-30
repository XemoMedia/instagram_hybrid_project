package com.xmedia.social.youtube.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultItem {
    private String kind;
    private String etag;
    private ResourceId id;
    private SearchSnippet snippet;
}
