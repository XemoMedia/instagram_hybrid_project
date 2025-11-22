package com.xmedia.social.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSentimentDto {
    private String text;
    private String sentiment; // "positive", "negative", "neutral"
    private Double polarity; // -1.0 to 1.0
}

