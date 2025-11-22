package com.xmedia.social.base.dto;

import lombok.Data;

@Data
public class CommentData {
    private String id;
    private String text;
    private String timestamp;
    private From from;
    private Replies replies;
}

