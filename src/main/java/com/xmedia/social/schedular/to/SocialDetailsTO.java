package com.xmedia.social.schedular.to;

import com.xmedia.social.schedular.enm.SocialPlatformEnum;

import lombok.Data;
@Data
public class SocialDetailsTO {
    private SocialPlatformEnum platform;
    private String profileId;
    private String token;
    private boolean enabled;
    private String status;
}
