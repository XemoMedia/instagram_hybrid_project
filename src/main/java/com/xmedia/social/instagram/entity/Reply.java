package com.xmedia.social.instagram.entity;

import java.time.LocalDateTime;

import com.xmedia.social.base.enums.SocialMediaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "replies")
public class Reply {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    private String accountId;

    private String username;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_media_type")
    private SocialMediaType socialMediaType;
}

