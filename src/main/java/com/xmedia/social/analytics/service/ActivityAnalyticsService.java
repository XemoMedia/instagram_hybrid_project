package com.xmedia.social.analytics.service;

import com.xmedia.social.analytics.dto.ActivityTypePointDto;
import com.xmedia.social.analytics.dto.DailyActivityTotalsDto;
import com.xmedia.social.analytics.dto.InstagramActivityResponseDto;
import com.xmedia.social.analytics.repository.ActivityAnalyticsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityAnalyticsService {

    private static final int DEFAULT_DAYS = 30;
    private final ActivityAnalyticsRepository repository;

    public InstagramActivityResponseDto fetchInstagramActivity(Integer days) {
        int safeDays = days == null ? DEFAULT_DAYS : Math.max(days, 1);
        LocalDate startDate = LocalDate.now().minusDays(safeDays - 1L);
        LocalDateTime startDateTime = startDate.atStartOfDay();

        List<DailyActivityTotalsDto> totals =
            repository.fetchDailyTotals(startDateTime).stream()
                .map(
                    row ->
                        DailyActivityTotalsDto.builder()
                            .activityDate(row.getActivity_date().toLocalDate())
                            .postCount(value(row.getPost_count()))
                            .commentCount(value(row.getComment_count()))
                            .replyCount(value(row.getReply_count()))
                            .build())
                .toList();

        List<ActivityTypePointDto> series =
            repository.fetchActivityTypeSeries(startDateTime).stream()
                .map(
                    row ->
                        ActivityTypePointDto.builder()
                            .activityDate(row.getActivity_date().toLocalDate())
                            .activityType(row.getActivity_type())
                            .totalCount(value(row.getTotal_count()))
                            .build())
                .collect(Collectors.toList());

        return InstagramActivityResponseDto.builder().totals(totals).stackedSeries(series).build();
    }

    private long value(Long number) {
        return number == null ? 0L : number;
    }
}

