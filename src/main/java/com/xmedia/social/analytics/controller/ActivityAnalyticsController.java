package com.xmedia.social.analytics.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.analytics.dto.InstagramActivityResponseDto;
import com.xmedia.social.analytics.service.ActivityAnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Insights for Instagram content activity")
@RequiredArgsConstructor
public class ActivityAnalyticsController {

    private final ActivityAnalyticsService activityAnalyticsService;

    @GetMapping("/instagram/daily-activity")
    @Operation(summary = "Daily Instagram activity", description = "Returns daily counts of posts, comments, and replies")
    public InstagramActivityResponseDto getDailyActivity(
        @Parameter(description = "Number of days back to include", example = "30")
        @RequestParam(required = false) Integer days) {
        return activityAnalyticsService.fetchInstagramActivity(days);
    }
}

