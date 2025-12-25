package com.xmedia.social.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPostResponse {
    private boolean success;
    private String message;
    private Object data;
}

