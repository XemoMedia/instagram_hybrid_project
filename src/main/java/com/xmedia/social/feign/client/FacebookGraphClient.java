package com.xmedia.social.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xmedia.social.user.model.FacebookUser;

@FeignClient(name = "facebookGraph", url = "https://graph.facebook.com/v19.0")
public interface FacebookGraphClient {

	@GetMapping("/me")
	FacebookUser getUserInfo(@RequestParam("fields") String fields, @RequestParam("access_token") String accessToken);

}
