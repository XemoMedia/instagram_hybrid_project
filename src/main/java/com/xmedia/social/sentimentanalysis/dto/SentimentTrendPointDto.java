package com.xmedia.social.sentimentanalysis.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SentimentTrendPointDto {
    private OffsetDateTime bucket;
    private long positive;
    private long neutral;
    private long negative;
}

