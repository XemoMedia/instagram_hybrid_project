package com.xmedia.social.base.dto;

import lombok.Data;

@Data
public class ReplyData {
    private String id;
    private String text;
    private String timestamp;
    private From from;
}

