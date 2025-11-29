package com.xmedia.social.sentimentanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
public class SentimentSampleDto {
    private String id;
    private String sourceId;
    private String sourceType;
    private String sentiment;
    private Double sentimentScore;
    private String topEmotion;
    private String analyzedText;
    private OffsetDateTime createdAt;
}

