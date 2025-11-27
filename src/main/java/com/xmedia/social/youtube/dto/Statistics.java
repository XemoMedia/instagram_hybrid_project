package com.xmedia.social.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private String viewCount;
    private String subscriberCount;
    private boolean hiddenSubscriberCount;
    private String videoCount;
}
