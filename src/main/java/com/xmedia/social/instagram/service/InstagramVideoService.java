package com.xmedia.social.instagram.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.VideoStatus;
import com.xmedia.social.instagram.dto.ApiPostResponse;
import com.xmedia.social.instagram.dto.InstagramVideoPost;
import com.xmedia.social.instagram.repository.InstagramVideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramVideoService {

	private final InstagramVideoRepository repository;
	private final InstagramVideoAsyncService asyncService;

	public ApiPostResponse createVideoPost(String videoUrl, String caption) {

		InstagramVideoPost post = new InstagramVideoPost();
		post.setVideoUrl(videoUrl);
		post.setCaption(caption);
		post.setStatus(VideoStatus.CREATED);
		post.setRetryCount(0);
		post.setCreatedAt(LocalDateTime.now());

		repository.save(post);

		asyncService.processVideoAsync(post);

		return new ApiPostResponse(true, "Video upload started", post.getId());
	}
	
	public ApiPostResponse getLatestVideoStatus() {

	    InstagramVideoPost post = repository.findTopByOrderByCreatedAtDesc()
	            .orElseThrow(() -> new RuntimeException("No video posts found"));

	    return new ApiPostResponse(
	            true,
	            post.getStatus().name(),
	            buildResponse(post)
	    );
	}



    private Map<String, Object> buildResponse(InstagramVideoPost post) {

        Map<String, Object> data = new HashMap<>();
        data.put("id", post.getId());
        data.put("status", post.getStatus());
        data.put("creationId", post.getCreationId());
        data.put("publishId", post.getPublishId());
        data.put("retryCount", post.getRetryCount());
        data.put("videoUrl", post.getVideoUrl());
        data.put("caption", post.getCaption());
        data.put("createdAt", post.getCreatedAt());

        return data;
    }
}
