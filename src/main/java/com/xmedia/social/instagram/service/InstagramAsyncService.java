package com.xmedia.social.instagram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.PostStatus;
import com.xmedia.social.feign.client.FacebookPostGraphClient;
import com.xmedia.social.instagram.dto.InstagramMediaResponse;
import com.xmedia.social.instagram.dto.InstagramPost;
import com.xmedia.social.instagram.dto.InstagramPublishResponse;
import com.xmedia.social.instagram.repository.InstagramPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramAsyncService {

    private final FacebookPostGraphClient graphClient;
    private final InstagramPostRepository repository;

    @Value("${ig-account-id}")
	private String igUserId;

	@Value("${access-token}")
	private String accessToken;

    @Async
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )
    public void processPostAsync(InstagramPost post) {

        try {
            post.setStatus(PostStatus.IN_PROGRESS);
            repository.save(post);

            // CREATE MEDIA
            InstagramMediaResponse media =
                    graphClient.createMedia(
                            igUserId,
                            post.getImageUrl(),
                            post.getCaption(),
                            accessToken
                    );

            post.setCreationId(media.getId());
            repository.save(post);

            // PUBLISH MEDIA
            InstagramPublishResponse publish =
                    graphClient.publishMedia(
                            igUserId,
                            media.getId(),
                            accessToken
                    );

            post.setPublishId(publish.getId());
            post.setStatus(PostStatus.PUBLISHED);
            repository.save(post);

        } catch (Exception ex) {
            post.setRetryCount(post.getRetryCount() + 1);
            post.setStatus(PostStatus.FAILED);
            repository.save(post);
            throw ex;
        }
    }
}

