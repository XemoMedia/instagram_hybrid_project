package com.xmedia.social.instagram.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instagram_replies")
public class InstagramReply {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    private String fromId;

    private String fromUsername;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private InstagramComment comment;
}

