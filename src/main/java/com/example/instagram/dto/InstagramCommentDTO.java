package com.example.instagram.dto;

import java.time.LocalDateTime;

public class InstagramCommentDTO {
    private String id;
    private String text;
    private LocalDateTime timestamp;
    private String fromId;
    private String fromUsername;
    private String mediaId;

    // Constructors
    public InstagramCommentDTO() {
    }

    public InstagramCommentDTO(String id, String text, LocalDateTime timestamp, String fromId, String fromUsername, String mediaId) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.fromId = fromId;
        this.fromUsername = fromUsername;
        this.mediaId = mediaId;
    }

    // Getters and Setters
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
