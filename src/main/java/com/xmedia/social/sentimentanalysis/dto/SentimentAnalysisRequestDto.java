package com.xmedia.social.sentimentanalysis.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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
