package com.xmedia.social.schedular.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "scheduled_post")
public class ScheduledPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String caption;

    private String imageUrl;
    private String videoUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "scheduled_post_socials",
        joinColumns = @JoinColumn(name = "scheduled_post_id")
    )
    private List<SocialDetailsEntity> platforms = new ArrayList<>();

    private LocalDateTime scheduledTime;

    private String status;
}