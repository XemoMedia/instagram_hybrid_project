package com.example.instagram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "insta_comments")
public class InstaComment {

    @Id
    @Column(length = 64)
    private String id;         // comment_id

    @Column(name = "media_id")
    private String mediaId;

    @Column(columnDefinition = "text")
    private String text;

    @Column(name = "username")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_time")
    private String timestamp;

    public InstaComment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMediaId() { return mediaId; }
    public void setMediaId(String mediaId) { this.mediaId = mediaId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
