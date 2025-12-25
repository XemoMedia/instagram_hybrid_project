package com.xmedia.social.analytics.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstagramActivityResponseDto {
    List<DailyActivityTotalsDto> totals;
    List<ActivityTypePointDto> stackedSeries;
}

