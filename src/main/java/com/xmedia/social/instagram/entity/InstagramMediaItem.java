package com.xmedia.social.instagram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InstagramMediaItem {

    @Id
    private String id;
    private String igAccountId;
    private LocalDateTime timestamp;
    private String caption;
}
