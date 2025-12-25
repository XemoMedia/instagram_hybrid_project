package com.xmedia.social.upload.entity;

import com.xmedia.social.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "media_analysis_analytics")
public class MediaAnalysisAnalytic extends BaseEntity {

	@Id
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "social_comment_analysis_id", nullable = false)
	private SocialCommentAnalysis socialCommentAnalysis;

	@Column(length = 64)
	private String language;

	@Column(name = "is_code_switched")
	private Boolean codeSwitched;

	@Column(name = "is_transliterated")
	private Boolean transliterated;

	@Column(length = 64)
	private String sentiment;

	@Column(length = 64)
	private String intent;

	@Column(length = 64)
	private String toxicity;

	@Column(length = 64)
	private String sarcasm;

	@Column(length = 64)
	private String emotion;

	@Column(columnDefinition = "TEXT")
	private String topics;

	@Column(columnDefinition = "TEXT")
	private String entities;

	@Column(columnDefinition = "TEXT")
	private String notes;
}

