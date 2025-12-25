package com.xmedia.social.sentimentanalysis.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending sentiment analysis request to Python REST endpoint.
 * Matches the Python SentimentAnalysisRequest schema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisRequestDto {
    private List<String> commentIds;
    private List<String> repliedIds;
}
