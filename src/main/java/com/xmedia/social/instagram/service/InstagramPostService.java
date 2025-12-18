package com.xmedia.social.instagram.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.PostStatus;
import com.xmedia.social.feign.client.FacebookPostGraphClient;
import com.xmedia.social.instagram.dto.ApiPostResponse;
import com.xmedia.social.instagram.dto.InstagramPost;
import com.xmedia.social.instagram.repository.InstagramPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramPostService {

	private final InstagramPostRepository repository;
	private final InstagramAsyncService asyncService;

	public ApiPostResponse createAndPublishPost(String imageUrl, String caption) {

		InstagramPost post = new InstagramPost();
		post.setImageUrl(imageUrl);
		post.setCaption(caption);
		post.setStatus(PostStatus.CREATED);
		post.setRetryCount(0);
		post.setCreatedAt(LocalDateTime.now());

		repository.save(post);

		asyncService.processPostAsync(post);

		return new ApiPostResponse(true, "Post queued for publishing", post.getId());
	}
}

//		try {
//			// STEP 1: CREATE MEDIA
//			InstagramMediaResponse mediaResponse = graphClient.createMedia(igUserId, imageUrl, caption, accessToken);
//
//			if (mediaResponse == null || mediaResponse.getId() == null) {
//				return new ApiPostResponse(false, "Media creation failed", null);
//			}
//
//			// STEP 2: PUBLISH MEDIA
//			InstagramPublishResponse publishResponse = graphClient.publishMedia(igUserId, mediaResponse.getId(),
//					accessToken);
//
//			if (publishResponse == null || publishResponse.getId() == null) {
//				return new ApiPostResponse(false, "Media publish failed", null);
//			}
//
//			return new ApiPostResponse(true, "Instagram post published successfully", publishResponse);
//
//		} catch (Exception e) {
//			return new ApiPostResponse(false, "Error while publishing post: " + e.getMessage(), null);
//		}
//	}
