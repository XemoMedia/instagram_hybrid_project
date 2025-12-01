package com.xmedia.social.sentimentanalysis.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentSentimentRequestDto {
    @NotEmpty(message = "Comments list cannot be empty")
    private List<String> comments;
}

