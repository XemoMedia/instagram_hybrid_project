package com.xmedia.social.youtube.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCommentsResponse {
    private String videoId;
    private List<CommentThreadItem> comments;
}

