package com.xmedia.social.instagram.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.xmedia.social.base.enums.SocialMediaType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {

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

    private String languageCode;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "social_media_type")
    private SocialMediaType socialMediaType;
}
