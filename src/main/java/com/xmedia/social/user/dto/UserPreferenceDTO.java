package com.xmedia.social.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferenceDTO {
    private Long id;
    private String key;
    private String value;
}
