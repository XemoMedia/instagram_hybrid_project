package com.example.instagram.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "instagramClient", url = "https://graph.facebook.com/v19.0")
public interface InstagramFeignClient {

	@GetMapping("/{mediaId}")
	String fetchMedia(@PathVariable("mediaId") String mediaId, @RequestParam("access_token") String accessToken,
			@RequestParam("fields") String fields);
}
