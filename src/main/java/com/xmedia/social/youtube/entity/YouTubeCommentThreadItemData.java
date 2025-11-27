package com.xmedia.social.youtube.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "youtube_comment_thread_item_data")
public class YouTubeCommentThreadItemData {

	@Id
	@Column(name = "comment_thread_item_id")
	private String id; // The ID of the comment thread

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "youtube_comment_thread_dat-id", nullable = false)
	@JsonIgnore
	private YouTubeCommentThreadDataEntity youTubeCommentThreadData;

	private String kind;
	private String etag;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
