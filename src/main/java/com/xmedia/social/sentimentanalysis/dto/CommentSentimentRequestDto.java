package com.xmedia.social.sentimentanalysis.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class CommentSentimentRequestDto {
    @NotEmpty(message = "Comments list cannot be empty")
    private List<String> comments;
}

