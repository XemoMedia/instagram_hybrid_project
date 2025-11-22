package com.xmedia.social.sentimentanalysis.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class PostSentimentRequestDto {
    @NotEmpty(message = "Posts list cannot be empty")
    private List<PostPayloadDto> posts;
}

