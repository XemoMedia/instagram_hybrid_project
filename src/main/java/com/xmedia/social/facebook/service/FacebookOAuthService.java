
package com.xmedia.social.facebook.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xmedia.social.feign.client.InstagramFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacebookOAuthService {

	@Value("${facebook.app-id}")
	private String appId;

	@Value("${facebook.app-secret}")
	private String appSecret;

	@Value("${facebook.redirect-uri}")
	private String redirectUri;

	private final InstagramFeignClient feignClient;

	public void processOAuth(String code) {

		Map<String, Object> response = feignClient.getAccessToken(appId, redirectUri, appSecret, code);

//    String tokenUrl =
//        "https://graph.facebook.com/v19.0/oauth/access_token" +
//        "?client_id=" + appId +
//        "&redirect_uri=" + redirectUri +
//        "&client_secret=" + appSecret +
//        "&code=" + code;
//
//    Map token = restTemplate.getForObject(tokenUrl, Map.class);
//    String userAccessToken = (String) token.get("access_token");

		String accessToken = (String) response.get("access_token");

		System.out.println("User Token = " + accessToken);
	}
}
