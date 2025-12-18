package com.xmedia.social.instagram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.VideoStatus;
import com.xmedia.social.feign.client.FacebookPostGraphClient;
import com.xmedia.social.instagram.dto.InstagramPublishResponse;
import com.xmedia.social.instagram.dto.InstagramVideoPost;
import com.xmedia.social.instagram.dto.MediaCreationResponse;
import com.xmedia.social.instagram.dto.MediaStatusResponse;
import com.xmedia.social.instagram.repository.InstagramVideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramVideoAsyncService {

    private final FacebookPostGraphClient graphClient;
    private final InstagramVideoRepository repository;

    @Value("${ig-account-id}")
    private String igUserId;

    @Value("${access-token}")
    private String accessToken;

    @Async
    @Retryable(
        retryFor = Exception.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 10000)
    )
    public void processVideoAsync(InstagramVideoPost post) {

        try {
            // STEP 1: CREATE VIDEO CONTAINER
            MediaCreationResponse response =
                graphClient.createVideoMedia(
                    igUserId,
                    "VIDEO",
                    post.getVideoUrl(),
                    post.getCaption(),
                    accessToken
                );

            post.setCreationId(response.getId());
            post.setStatus(VideoStatus.IN_PROGRESS);
            repository.save(post);

            // STEP 2: CHECK STATUS
            MediaStatusResponse statusResponse =
                graphClient.getMediaStatus(
                    response.getId(),
                    "status_code",
                    accessToken
                );

            if ("IN_PROGRESS".equals(statusResponse.getStatus_code())) {
                throw new RuntimeException("Video still processing");
            }

            if ("ERROR".equals(statusResponse.getStatus_code())) {
                post.setStatus(VideoStatus.ERROR);
                repository.save(post);
                return;
            }

            if ("FINISHED".equals(statusResponse.getStatus_code())) {

                post.setStatus(VideoStatus.FINISHED);
                repository.save(post);

                // STEP 3: PUBLISH
                InstagramPublishResponse publish =
                    graphClient.publishMedia(
                        igUserId,
                        post.getCreationId(),
                        accessToken
                    );

                post.setPublishId(publish.getId());
                post.setStatus(VideoStatus.PUBLISHED);
                repository.save(post);
            }

        } catch (Exception ex) {
            post.setRetryCount(post.getRetryCount() + 1);
            repository.save(post);
            throw ex;
        }
    }
}

