package com.example.instagram.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "instagram_comments")
public class InstagramComment {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    private String fromId;

    private String fromUsername;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private InstagramMedia media;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstagramReply> replies = new ArrayList<>();

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

	public InstagramMedia getMedia() {
		return media;
	}

	public void setMedia(InstagramMedia media) {
		this.media = media;
	}

	public List<InstagramReply> getReplies() {
		return replies;
	}

	public void setReplies(List<InstagramReply> replies) {
		this.replies = replies;
	}
    
    
    
}


/*Entity
public class InstagramComment {
    @Id
    private String id;
    private String text;
    private LocalDateTime timestamp;
    private String fromId;
    private String fromUsername;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medi-id")
    private InstagramMedia media;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstagramReply> replies;

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

    public InstagramMedia getMedia() {
        return media;
    }

    public void setMedia(InstagramMedia media) {
        this.media = media;
    }

    public List<InstagramReply> getReplies() {
        return replies;
    }

    public void setReplies(List<InstagramReply> replies) {
        this.replies = replies;
    }
}
*/