package com.xmedia.social.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xmedia.social.instagram.dto.InstagramMediaResponse;
import com.xmedia.social.instagram.dto.InstagramPublishResponse;
import com.xmedia.social.instagram.dto.MediaCreationResponse;
import com.xmedia.social.instagram.dto.MediaPublishResponse;
import com.xmedia.social.instagram.dto.MediaStatusResponse;

@FeignClient(name = "facebookGraphClient", url = "https://graph.facebook.com/v19.0")
public interface FacebookPostGraphClient {

	@PostMapping(value = "/{igUserId}/media", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	InstagramMediaResponse createMedia(@PathVariable String igUserId, @RequestParam("image_url") String imageUrl,
			@RequestParam("caption") String caption, @RequestParam("access_token") String accessToken);

	@PostMapping(value = "/{igUserId}/media_publish", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	InstagramPublishResponse publishMedia(@PathVariable String igUserId, @RequestParam("creation_id") String creationId,
			@RequestParam("access_token") String accessToken);

	@PostMapping(value = "/{igUserId}/media", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	MediaCreationResponse createVideoMedia(@PathVariable String igUserId, @RequestParam("media_type") String mediaType,
			@RequestParam("video_url") String videoUrl, @RequestParam("caption") String caption,
			@RequestParam("access_token") String accessToken);

	@GetMapping(value = "/{creationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	MediaStatusResponse getMediaStatus(@PathVariable String creationId, @RequestParam("fields") String fields,
			@RequestParam("access_token") String accessToken);

//	@PostMapping(value = "/{igUserId}/media_publish", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	MediaPublishResponse publishMedia(@PathVariable String igUserId, @RequestParam("creation_id") String creationId,
//			@RequestParam("access_token") String accessToken);
}
