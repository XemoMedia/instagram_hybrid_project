package com.xmedia.social.youtube.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
