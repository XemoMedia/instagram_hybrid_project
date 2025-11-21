package com.example.instagram.dto;

import lombok.Data;
import java.util.List;

@Data
public class InstagramMediaListResponseDto {
    private List<MediaItem> data;
    private Paging paging;

    @Data
    public static class MediaItem {
        private String id;
        private String timestamp;
        private String caption;
        
    }

    @Data
    public static class Paging {
        private Cursors cursors;
        private String next;
        private String previous;

        @Data
        public static class Cursors {
            private String before;
            private String after;
        }
    }
}
