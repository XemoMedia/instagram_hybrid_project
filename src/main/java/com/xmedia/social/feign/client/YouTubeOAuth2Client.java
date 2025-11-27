package com.xmedia.social.feign.client;

import com.xmedia.social.youtube.dto.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-oauth2", url = "https://oauth2.googleapis.com")
public interface YouTubeOAuth2Client {

    @PostMapping("/token")
    GoogleTokenResponse getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri
    );

    @PostMapping("/token")
    GoogleTokenResponse refreshAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("grant_type") String grantType
    );
}
