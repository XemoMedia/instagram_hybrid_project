package com.xmedia.social.sentimentanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
public class SentimentTrendPointDto {
    private OffsetDateTime bucket;
    private long positive;
    private long neutral;
    private long negative;
}

