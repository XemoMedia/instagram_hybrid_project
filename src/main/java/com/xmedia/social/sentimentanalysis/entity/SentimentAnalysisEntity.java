package com.xmedia.social.sentimentanalysis.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sentiment_analysis")
@Getter
@Setter
public class SentimentAnalysisEntity {

    @Id
    private String id;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(name = "source_type", nullable = false)
    private String sourceType;

    @Column(nullable = false)
    private String sentiment;

    @Column(name = "sentiment_score")
    private BigDecimal sentimentScore;

    @Column(name = "top_emotion")
    private String topEmotion;

    @Column(name = "emotion_scores", columnDefinition = "jsonb")
    private String emotionScores;

    @Column(name = "analyzed_text", columnDefinition = "text")
    private String analyzedText;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}

