package com.xmedia.social.youtube.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AuthorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true) // Assuming authorChannelId.value is unique for an author
	private String authorChannelValue;
	private String authorDisplayName;
	private String authorProfileImageUrl;
	private String authorChannelUrl;

}
