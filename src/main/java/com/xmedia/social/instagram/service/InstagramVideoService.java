package com.xmedia.social.instagram.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.VideoStatus;
import com.xmedia.social.instagram.dto.ApiPostResponse;
import com.xmedia.social.instagram.dto.InstagramVideoPost;
import com.xmedia.social.instagram.repository.InstagramVideoRepository;

import io.swagger.v3.oas.models.responses.ApiResponse;
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
}
