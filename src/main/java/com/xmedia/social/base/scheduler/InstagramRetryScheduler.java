package com.xmedia.social.base.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xmedia.social.base.enums.VideoStatus;
import com.xmedia.social.instagram.dto.InstagramVideoPost;
import com.xmedia.social.instagram.repository.InstagramPostRepository;
import com.xmedia.social.instagram.repository.InstagramVideoRepository;
import com.xmedia.social.instagram.service.InstagramAsyncService;
import com.xmedia.social.instagram.service.InstagramVideoAsyncService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InstagramRetryScheduler {

	private final InstagramPostRepository repository;
	private final InstagramAsyncService asyncService;
	 private final InstagramVideoRepository instagramVideoRepository;
	
	private final InstagramVideoAsyncService instagramVideoAsyncService;
	
	@Scheduled(fixedDelay = 15000) // every 15 seconds
    public void retryInProgressVideos() {

        List<InstagramVideoPost> posts =
        		instagramVideoRepository.findByStatusIn(
                        List.of(VideoStatus.IN_PROGRESS)
                );

        for (InstagramVideoPost post : posts) {
        	instagramVideoAsyncService.processVideoAsync(post);
        }
    }

//	@Scheduled(fixedDelay = 60000) // every 1 min
//	public void retryFailedPosts() {
//
//		List<InstagramPost> posts = repository.findByStatusIn(List.of(PostStatus.FAILED, PostStatus.IN_PROGRESS));
//
//		for (InstagramPost post : posts) {
//			if (post.getRetryCount() < 3) {
//				asyncService.processPostAsync(post);
//			}
//		}
//	}
}
