package com.example.instagram.entity;

import java.util.List;

public class InstagramResponseDto {
    private List<InstagramDataItemDto> data;

    public List<InstagramDataItemDto> getData() {
        return data;
    }

    public void setData(List<InstagramDataItemDto> data) {
        this.data = data;
    }

    public static class InstagramDataItemDto {
        private String id;
        private String text;
        private String timestamp;
        private InstagramFromDto from;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public InstagramFromDto getFrom() {
            return from;
        }

        public void setFrom(InstagramFromDto from) {
            this.from = from;
        }
    }

    public static class InstagramFromDto {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
