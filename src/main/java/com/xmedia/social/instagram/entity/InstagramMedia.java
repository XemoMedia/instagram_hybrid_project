package com.xmedia.social.instagram.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instagram_media")
public class InstagramMedia {

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

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstagramComment> comments = new ArrayList<>();
}
