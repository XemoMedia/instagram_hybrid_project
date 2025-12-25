package com.xmedia.social.sentimentanalysis.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

