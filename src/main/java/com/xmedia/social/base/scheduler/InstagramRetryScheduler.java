package com.xmedia.social.base.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xmedia.social.base.enums.PostStatus;
import com.xmedia.social.instagram.dto.InstagramPost;
import com.xmedia.social.instagram.repository.InstagramPostRepository;
import com.xmedia.social.instagram.service.InstagramAsyncService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InstagramRetryScheduler {

	private final InstagramPostRepository repository;
	private final InstagramAsyncService asyncService;

	@Scheduled(fixedDelay = 60000) // every 1 min
	public void retryFailedPosts() {

		List<InstagramPost> posts = repository.findByStatusIn(List.of(PostStatus.FAILED, PostStatus.IN_PROGRESS));

		for (InstagramPost post : posts) {
			if (post.getRetryCount() < 3) {
				asyncService.processPostAsync(post);
			}
		}
	}
}
