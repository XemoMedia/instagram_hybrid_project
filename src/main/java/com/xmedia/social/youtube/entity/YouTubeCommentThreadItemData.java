package com.xmedia.social.youtube.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
