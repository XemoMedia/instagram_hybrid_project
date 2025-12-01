package com.xmedia.social.youtube.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "youtube_video_list")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchDataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uniqueId;

	@Column(name = "video_id")
	private String videoId;

	@Column(name = "channel_id")
	private String channelId;

	@CreationTimestamp
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
}
