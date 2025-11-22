package com.xmedia.social.sentimentanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponseDto {
    private List<SentimentResultDto> results;
}

