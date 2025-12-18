package com.xmedia.social.feign.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "instagramClient", url = "https://graph.facebook.com/v19.0")
public interface InstagramFeignClient {

	@GetMapping(value = "/{mediaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	String fetchMedia(@PathVariable("mediaId") String mediaId, @RequestParam("access_token") String accessToken,
			@RequestParam("fields") String fields);

	@GetMapping(value = "/{igAccountId}/media", produces = MediaType.APPLICATION_JSON_VALUE)
	String fetchMediaList(@PathVariable("igAccountId") String igAccountId, @RequestParam("fields") String fields,
			@RequestParam("access_token") String accessToken);

	@GetMapping("/oauth/access_token")
	Map<String, Object> getAccessToken(@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri, @RequestParam("client_secret") String clientSecret,
			@RequestParam("code") String code);
}
