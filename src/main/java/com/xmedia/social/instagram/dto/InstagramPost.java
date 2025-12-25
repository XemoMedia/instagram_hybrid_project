package com.xmedia.social.instagram.dto;

import java.time.LocalDateTime;

import com.xmedia.social.base.enums.PostStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "instagram_posts")
@Data
public class InstagramPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String caption;

    private String creationId;

    private String publishId;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private int retryCount;

    private LocalDateTime createdAt;
}

