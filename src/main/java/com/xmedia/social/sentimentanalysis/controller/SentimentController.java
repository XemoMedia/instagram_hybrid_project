package com.xmedia.social.sentimentanalysis.controller;

import com.xmedia.social.sentimentanalysis.dto.SentimentDashboardResponseDto;
import com.xmedia.social.sentimentanalysis.service.SentimentDashboardService;
import com.xmedia.social.sentimentanalysis.service.SentimentService;
import com.xmedia.social.instagram.service.InstagramCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for sentiment analysis endpoints.
 */
@RestController
@RequestMapping("/api/v1/sentiment")
@Tag(name = "Sentiment Analysis", description = "Endpoints for analyzing sentiment of comments and posts")
@RequiredArgsConstructor
public class SentimentController {

   
    private final SentimentDashboardService sentimentDashboardService;

    @GetMapping("/dashboard")
    @Operation(summary = "Sentiment analytics dashboard", description = "Aggregated sentiment metrics for visualization")
    public SentimentDashboardResponseDto getDashboard(
        @Parameter(description = "Number of days to include in the calculations", example = "30")
        @RequestParam(defaultValue = "30") Integer days
    ) {
        return sentimentDashboardService.fetchDashboard(days);
    }
}


