package com.xmedia.social.sentimentanalysis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SentimentDashboardResponseDto {
    private SentimentCountsDto counts;
    private List<SentimentTrendPointDto> trend;
    private List<SourceBreakdownDto> sources;
    private List<EmotionShareDto> emotions;
    private List<SentimentSampleDto> samples;
}

