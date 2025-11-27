package com.xmedia.social.googleauth.service;

import com.xmedia.social.feign.client.GoogleTokenFeignClient;
import com.xmedia.social.googleauth.model.GoogleToken;
import com.xmedia.social.googleauth.repository.GoogleTokenRepository;
import com.xmedia.social.youtube.dto.GoogleTokenResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    @Value("${google.oauth2.redirect-uri}")
    private String redirectUri;

    private final GoogleTokenFeignClient googleTokenFeignClient;
    private final GoogleTokenRepository googleTokenRepository;

//    public GoogleAuthService(GoogleTokenFeignClient googleTokenFeignClient) {
//        this.googleTokenFeignClient = googleTokenFeignClient;
//    }

    public GoogleTokenResponse exchangeCodeForTokens(String authorizationCode) {
        GoogleTokenResponse tokenResponse = googleTokenFeignClient.exchangeAuthorizationCodeForTokens(
                clientId,
                clientSecret,
                authorizationCode,
                "authorization_code",
                redirectUri
        );
        saveGoogleToken(tokenResponse);
        return tokenResponse;
    }

    private void saveGoogleToken(GoogleTokenResponse tokenResponse) {
        GoogleToken googleToken = GoogleToken.builder()
                .accessToken(tokenResponse.getAccessToken())
                .expiresIn(tokenResponse.getExpiresIn())
                .scope(tokenResponse.getScope())
                .tokenType(tokenResponse.getTokenType())
                .idToken(tokenResponse.getIdToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .build();
        googleTokenRepository.save(googleToken);
    }
}
