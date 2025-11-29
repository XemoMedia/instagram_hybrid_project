package com.xmedia.social.sentimentanalysis.service;

import com.xmedia.social.sentimentanalysis.dto.*;
import com.xmedia.social.sentimentanalysis.entity.SentimentAnalysisEntity;
import com.xmedia.social.sentimentanalysis.repository.SentimentAnalysisRepository;

import com.xmedia.social.sentimentanalysis.repository.projection.SentimentCountProjection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentDashboardService {

    private static final int DEFAULT_DAYS = 30;
    private static final int MAX_EMOTIONS = 6;
    private final SentimentAnalysisRepository repository;

    public SentimentDashboardResponseDto fetchDashboard(Integer days) {
        int safeDays = days == null ? DEFAULT_DAYS : Math.max(days, 1);
        OffsetDateTime startDate = OffsetDateTime.now().minusDays(safeDays);

        SentimentCountsDto countsDto = buildCounts(startDate);
        List<SentimentTrendPointDto> trend = buildTrend(startDate);
        List<SourceBreakdownDto> sources = buildSources(startDate);
        List<EmotionShareDto> emotions = buildEmotions(startDate);
        List<SentimentSampleDto> samples = buildSamples();

        return SentimentDashboardResponseDto.builder()
            .counts(countsDto)
            .trend(trend)
            .sources(sources)
            .emotions(emotions)
            .samples(samples)
            .build();
    }

    private SentimentCountsDto buildCounts(OffsetDateTime startDate) {
        List<SentimentCountProjection> rows = repository.fetchSentimentCounts(startDate);
        Map<String, Long> mapped = rows.stream()
            .collect(Collectors.toMap(
                row -> row.getSentiment() == null ? "neutral" : row.getSentiment(),
                SentimentCountProjection::getCount
            ));

        return SentimentCountsDto.builder()
            .positive(mapped.getOrDefault("positive", 0L))
            .neutral(mapped.getOrDefault("neutral", 0L))
            .negative(mapped.getOrDefault("negative", 0L))
            .build();
    }

    private List<SentimentTrendPointDto> buildTrend(OffsetDateTime startDate) {
        ZoneId zone = ZoneId.systemDefault();
        return repository.fetchTrend(startDate).stream()
            .map(row -> SentimentTrendPointDto.builder()
                .bucket(row.getBucket().toInstant().atZone(zone).toOffsetDateTime())
                .positive(safeValue(row.getPositive()))
                .neutral(safeValue(row.getNeutral()))
                .negative(safeValue(row.getNegative()))
                .build())
            .toList();
    }

    private List<SourceBreakdownDto> buildSources(OffsetDateTime startDate) {
        return repository.fetchSourceBreakdown(startDate).stream()
            .map(row -> SourceBreakdownDto.builder()
                .sourceType(row.getSource_type())
                .total(safeValue(row.getTotal()))
                .positive(safeValue(row.getPositive()))
                .neutral(safeValue(row.getNeutral()))
                .negative(safeValue(row.getNegative()))
                .averageScore(row.getAvg_score())
                .build())
            .toList();
    }

    private List<EmotionShareDto> buildEmotions(OffsetDateTime startDate) {
        return repository.fetchTopEmotions(startDate, MAX_EMOTIONS).stream()
            .map(row -> EmotionShareDto.builder()
                .emotion(row.getEmotion())
                .total(safeValue(row.getTotal()))
                .build())
            .toList();
    }

    private List<SentimentSampleDto> buildSamples() {
        return repository.findTop6ByOrderByCreatedAtDesc().stream()
            .map(this::mapSample)
            .toList();
    }

    private SentimentSampleDto mapSample(SentimentAnalysisEntity entity) {
        return SentimentSampleDto.builder()
            .id(entity.getId())
            .sourceId(entity.getSourceId())
            .sourceType(entity.getSourceType())
            .sentiment(entity.getSentiment())
            .sentimentScore(entity.getSentimentScore() != null ? entity.getSentimentScore().doubleValue() : null)
            .topEmotion(entity.getTopEmotion())
            .analyzedText(entity.getAnalyzedText())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private long safeValue(Long value) {
        return value == null ? 0L : value;
    }
}

