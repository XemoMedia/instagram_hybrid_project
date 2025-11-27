package com.xmedia.social.facebook.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FacebookCommentResponse {
    private List<CommentData> data;

    @Getter
    @Setter
    public static class CommentData {
        private String id;
        private String message;
        private From from;
        private String created_time;
    }

    @Getter
    @Setter
    public static class From {
        private String name;
        private String id;
    }
}
