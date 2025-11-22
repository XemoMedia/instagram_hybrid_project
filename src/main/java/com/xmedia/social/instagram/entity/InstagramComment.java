package com.xmedia.social.instagram.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "instagram_comments")
public class InstagramComment {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime timestamp;

    private String fromId;

    private String fromUsername;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private InstagramMedia media;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstagramReply> replies = new ArrayList<>();
}
