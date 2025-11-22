package com.xmedia.social.instagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmedia.social.base.dto.Comments;
import lombok.Data;

@Data
public class InstagramResponseDto {
    private String id;
    
    @JsonProperty("media_type")
    private String mediaType;
    
    @JsonProperty("media_url")
    private String mediaUrl;
    
    @JsonProperty("permalink")
    private String permalink;
    
    private String timestamp;
    private String username;
    
    @JsonProperty("like_count")
    private int likeCount;
    
    private Comments comments;
}
