package com.xmedia.social.sentimentanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SourceBreakdownDto {
    private String sourceType;
    private long total;
    private long positive;
    private long neutral;
    private long negative;
    private Double averageScore;
}

