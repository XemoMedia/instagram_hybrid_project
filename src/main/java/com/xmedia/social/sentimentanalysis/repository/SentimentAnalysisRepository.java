package com.xmedia.social.sentimentanalysis.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xmedia.social.sentimentanalysis.entity.SentimentAnalysisEntity;
import com.xmedia.social.sentimentanalysis.repository.projection.EmotionShareProjection;
import com.xmedia.social.sentimentanalysis.repository.projection.SentimentCountProjection;
import com.xmedia.social.sentimentanalysis.repository.projection.SentimentTrendProjection;
import com.xmedia.social.sentimentanalysis.repository.projection.SourceBreakdownProjection;

public interface SentimentAnalysisRepository extends JpaRepository<SentimentAnalysisEntity, String> {

    @Query(
        value = """
            SELECT sentiment as sentiment, COUNT(*) as count
            FROM sentiment_analysis
            WHERE created_at >= :startDate
            GROUP BY sentiment
            """,
        nativeQuery = true
    )
    List<SentimentCountProjection> fetchSentimentCounts(@Param("startDate") OffsetDateTime startDate);

    @Query(
        value = """
            SELECT date_trunc('day', created_at) AS bucket,
                   SUM(CASE WHEN sentiment = 'positive' THEN 1 ELSE 0 END) AS positive,
                   SUM(CASE WHEN sentiment = 'neutral' THEN 1 ELSE 0 END) AS neutral,
                   SUM(CASE WHEN sentiment = 'negative' THEN 1 ELSE 0 END) AS negative
            FROM sentiment_analysis
            WHERE created_at >= :startDate
            GROUP BY bucket
            ORDER BY bucket
            """,
        nativeQuery = true
    )
    List<SentimentTrendProjection> fetchTrend(@Param("startDate") OffsetDateTime startDate);

    @Query(
        value = """
            SELECT source_type,
                   COUNT(*) AS total,
                   AVG(sentiment_score) AS avg_score,
                   SUM(CASE WHEN sentiment = 'positive' THEN 1 ELSE 0 END) AS positive,
                   SUM(CASE WHEN sentiment = 'neutral' THEN 1 ELSE 0 END) AS neutral,
                   SUM(CASE WHEN sentiment = 'negative' THEN 1 ELSE 0 END) AS negative
            FROM sentiment_analysis
            WHERE created_at >= :startDate
            GROUP BY source_type
            """,
        nativeQuery = true
    )
    List<SourceBreakdownProjection> fetchSourceBreakdown(@Param("startDate") OffsetDateTime startDate);

    @Query(
        value = """
            SELECT COALESCE(top_emotion, 'unknown') AS emotion,
                   COUNT(*) AS total
            FROM sentiment_analysis
            WHERE created_at >= :startDate
              AND top_emotion IS NOT NULL
            GROUP BY top_emotion
            ORDER BY total DESC
            LIMIT :limit
            """,
        nativeQuery = true
    )
    List<EmotionShareProjection> fetchTopEmotions(
        @Param("startDate") OffsetDateTime startDate,
        @Param("limit") int limit
    );

    List<SentimentAnalysisEntity> findTop6ByOrderByCreatedAtDesc();
}

