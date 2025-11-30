package com.xmedia.social.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xmedia.social.youtube.dto.YouTubeTokenResponse;

@FeignClient(name = "youtubeTokenFeignClient", url = "https://oauth2.googleapis.com")
public interface YouTubeTokenFeignClient {

    @PostMapping("/token")
    YouTubeTokenResponse exchangeAuthorizationCodeForTokens(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri
    );

    @PostMapping("/token")
    YouTubeTokenResponse refreshAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("grant_type") String grantType
    );
}
