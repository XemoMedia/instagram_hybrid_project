package com.xmedia.social.instagram.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstagramCommentDto {
    private String id;
    private String text;
    private LocalDateTime timestamp;
    private String fromId;
    private String fromUsername;
    private String mediaId;
}
