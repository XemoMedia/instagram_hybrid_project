package com.xmedia.social.sentimentanalysis.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostPayloadDto {
    private String postId;
    
    @NotBlank(message = "Caption cannot be blank")
    private String caption;
    
    private List<String> comments;
}

