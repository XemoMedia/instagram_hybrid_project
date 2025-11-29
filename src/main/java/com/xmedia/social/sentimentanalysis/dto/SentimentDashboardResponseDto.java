package com.xmedia.social.sentimentanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

