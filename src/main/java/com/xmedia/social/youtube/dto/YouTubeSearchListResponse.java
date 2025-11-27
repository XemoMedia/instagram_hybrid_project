package com.xmedia.social.youtube.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchListResponse {
    private String kind;
    private String etag;
    private String regionCode;
    private PageInfo pageInfo;
    private List<SearchItem> items;
}
