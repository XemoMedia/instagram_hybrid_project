package com.xmedia.social.schedular.entity;

import com.xmedia.social.schedular.enm.SocialPlatformEnum;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class SocialDetailsEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SocialPlatformEnum platform;

    private String profileId;

    private String token;

    private boolean enabled;
    private String status;
    
}