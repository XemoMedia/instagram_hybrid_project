package com.xmedia.social.sentimentanalysis.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostSentimentRequestDto {
    @NotEmpty(message = "Posts list cannot be empty")
    private List<PostPayloadDto> posts;
}

