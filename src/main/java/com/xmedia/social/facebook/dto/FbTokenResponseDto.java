package com.xmedia.social.facebook.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FbTokenResponseDto {

	private String accessToken;
	private LocalDateTime expiresAt;
}

