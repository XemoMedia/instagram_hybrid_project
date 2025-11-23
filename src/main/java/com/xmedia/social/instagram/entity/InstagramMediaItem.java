package com.xmedia.social.instagram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class InstagramMediaItem {

    @Id
    private String id;
    private String igAccountId;
    private LocalDateTime timestamp;
    private String caption;
}
