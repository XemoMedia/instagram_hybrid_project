package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelItem {
    private String kind;
    private String etag;
    private String id;
    private ChannelSnippet snippet;
    private ContentDetails contentDetails;
    private Statistics statistics;
}
