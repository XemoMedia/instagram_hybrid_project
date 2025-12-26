package com.xmedia.social.upload.entity;

import com.xmedia.social.base.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity that holds the enriched insight columns generated during upload processing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "social_comment_analysis")
public class SocialCommentAnalysis extends BaseEntity {

    @Id
    private String id;

    @Column(length = 128)
    private String username;
    
    @Column
    private String msgId;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(length = 64)
    private String platform;

    @Column(length = 128)
    private String brand;

	@OneToOne(mappedBy = "socialCommentAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
	private MediaAnalysisAnalytic analytics;
}
