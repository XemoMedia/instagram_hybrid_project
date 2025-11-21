package com.example.instagram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
public class InstagramMediaItem {

    @Id
    private String id;
    private String igAccountId;
    private LocalDateTime timestamp;
    private String caption;

    public InstagramMediaItem() {
    }

    public InstagramMediaItem(String id, String igAccountId, LocalDateTime timestamp, String caption) {
        this.id = id;
        this.igAccountId = igAccountId;
        this.timestamp = timestamp;
        this.caption = caption;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIgAccountId() {
        return igAccountId;
    }

    public void setIgAccountId(String igAccountId) {
        this.igAccountId = igAccountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

//    @Override
//    public String toString() {
//        return "InstagramMediaItem{" +
//               "id='" + id + '" +
//               ", igAccountId='" + igAccountId + ''' +
//               ", timestamp=" + timestamp +
//               ", caption='" + caption + ''' +
//               '}';
//    }
}
