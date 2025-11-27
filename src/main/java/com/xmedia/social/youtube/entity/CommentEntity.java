package com.xmedia.social.youtube.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CommentEntity {
	@Id
	private String id; // YouTube comment ID

	private String kind;
	private String etag;
	private String channelId;
	private String videoId;
	@Column(length = 2000) // Assuming textDisplay can be long
	private String textDisplay;
	@Column(length = 2000) // Assuming textOriginal can be long
	private String textOriginal;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private AuthorEntity author;

	private Boolean canRate;
	private String viewerRating;
	private Integer likeCount;
	private OffsetDateTime publishedAt;
	private OffsetDateTime updatedAt;

}
