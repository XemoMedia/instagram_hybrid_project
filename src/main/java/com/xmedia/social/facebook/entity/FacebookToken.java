package com.xmedia.social.facebook.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fb_tokens")
public class FacebookToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    private String tokenType;

    private LocalDateTime issuedAt;

    // expires_in as LocalDateTime
    private LocalDateTime expiresIn;

    private String appId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
