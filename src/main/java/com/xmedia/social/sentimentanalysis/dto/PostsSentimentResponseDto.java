package com.xmedia.social.sentimentanalysis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsSentimentResponseDto {
    private List<PostSentimentResultDto> results;
}

