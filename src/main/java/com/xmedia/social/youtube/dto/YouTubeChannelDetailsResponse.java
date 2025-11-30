package com.xmedia.social.youtube.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeChannelDetailsResponse {
    private String kind = "youtube#channelListResponse";
    private String etag;
    private PageInfo pageInfo;
    private List<Item> items;
}
