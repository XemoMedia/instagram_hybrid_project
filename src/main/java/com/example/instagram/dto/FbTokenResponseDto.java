package com.example.instagram.dto;

import java.time.LocalDateTime;

public class FbTokenResponseDto {

	private String accessToken;
	private LocalDateTime expiresAt;

	public FbTokenResponseDto(String accessToken, LocalDateTime expiresAt) {
		this.accessToken = accessToken;
		this.expiresAt = expiresAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
}
