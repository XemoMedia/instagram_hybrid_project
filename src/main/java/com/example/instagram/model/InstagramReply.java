package com.example.instagram.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "instagram_replies")
public class InstagramReply {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    private String fromId;

    private String fromUsername;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private InstagramComment comment;

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

	public InstagramComment getComment() {
		return comment;
	}

	public void setComment(InstagramComment comment) {
		this.comment = comment;
	}
    
    
}

//@Entity
//public class InstagramReply {
//    @Id
//    private String id;
//    private String text;
//    private LocalDateTime timestamp;
//    private String fromId;
//    private String fromUsername;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    private InstagramComment comment;
//
//    // Getters and Setters
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getFromId() {
//        return fromId;
//    }
//
//    public void setFromId(String fromId) {
//        this.fromId = fromId;
//    }
//
//    public String getFromUsername() {
//        return fromUsername;
//    }
//
//    public void setFromUsername(String fromUsername) {
//        this.fromUsername = fromUsername;
//    }
//
//    public InstagramComment getComment() {
//        return comment;
//    }
//
//    public void setComment(InstagramComment comment) {
//        this.comment = comment;
//    }
//}
