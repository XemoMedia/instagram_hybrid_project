package com.example.instagram.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "instagram_media")
public class InstagramMedia {

    @Id
    private String id;

    private String mediaType;

    @Column(columnDefinition = "TEXT")
    private String mediaUrl;

    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String caption;

    @Column(columnDefinition = "TEXT")
    private String permalink;

    private LocalDateTime timestamp;

    private String username;

    private Integer likeCount;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstagramComment> comments = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public List<InstagramComment> getComments() {
		return comments;
	}

	public void setComments(List<InstagramComment> comments) {
		this.comments = comments;
	}
    
    
}


//@Entity
//@Data
//public class InstagramMedia {
//    @Id
//    private String id;
//    private String mediaType;
//    private String mediaUrl;
//    private String permalink;
//    private LocalDateTime timestamp;
//    private String username;
//    private int likeCount;
//
//    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<InstagramComment> comments;
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
//    public String getMediaType() {
//        return mediaType;
//    }
//
//    public void setMediaType(String mediaType) {
//        this.mediaType = mediaType;
//    }
//
//    public String getMediaUrl() {
//        return mediaUrl;
//    }
//
//    public void setMediaUrl(String mediaUrl) {
//        this.mediaUrl = mediaUrl;
//    }
//
//    public String getPermalink() {
//        return permalink;
//    }
//
//    public void setPermalink(String permalink) {
//        this.permalink = permalink;
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
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public int getLikeCount() {
//        return likeCount;
//    }
//
//    public void setLikeCount(int likeCount) {
//        this.likeCount = likeCount;
//    }
//
//    public List<InstagramComment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<InstagramComment> comments) {
//        this.comments = comments;
//    }
//}
