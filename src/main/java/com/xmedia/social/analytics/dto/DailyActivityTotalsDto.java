package com.xmedia.social.analytics.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DailyActivityTotalsDto {
    LocalDate activityDate;
    long postCount;
    long commentCount;
    long replyCount;
}

