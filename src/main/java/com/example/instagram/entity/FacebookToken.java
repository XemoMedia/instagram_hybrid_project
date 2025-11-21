package com.example.instagram.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    // Getters & Setters
    public Long getId() { return id; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

   

    public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}

	public LocalDateTime getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(LocalDateTime expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }

   
    public LocalDateTime getCreatedAt() { return createdAt; }
}
