package com.xmedia.social.youtube.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import com.example.youtube.dto.YouTubePageInfo;
// import com.fasterxml.jackson.databind.ObjectMapper; // REMOVED THIS LINE
import com.xmedia.social.feign.client.YouTubeDataApiClient;
import com.xmedia.social.feign.client.YouTubeOAuth2Client;
import com.xmedia.social.youtube.dto.GoogleTokenResponse;
import com.xmedia.social.youtube.dto.YouTubeChannelListResponse;
import com.xmedia.social.youtube.dto.YouTubeCommentThreadListResponse;
import com.xmedia.social.youtube.dto.YouTubeSearchListResponse;
import com.xmedia.social.youtube.entity.YouTubeAuthorChannelIdEntity;
import com.xmedia.social.youtube.entity.YouTubeChannelDataEntity;
import com.xmedia.social.youtube.entity.YouTubeChannelItemEntity;
import com.xmedia.social.youtube.entity.YouTubeChannelSnippetEntity;
import com.xmedia.social.youtube.entity.YouTubeCommentEntity;
import com.xmedia.social.youtube.entity.YouTubeCommentSnippetEntity;
import com.xmedia.social.youtube.entity.YouTubeCommentThreadDataEntity;
import com.xmedia.social.youtube.entity.YouTubeCommentThreadItemEntity;
import com.xmedia.social.youtube.entity.YouTubeCommentThreadSnippetEntity;
import com.xmedia.social.youtube.entity.YouTubeContentDetailsEntity;
import com.xmedia.social.youtube.entity.YouTubeLocalized;
import com.xmedia.social.youtube.entity.YouTubePageInfo;
import com.xmedia.social.youtube.entity.YouTubeRelatedPlaylists;
import com.xmedia.social.youtube.entity.YouTubeReplies;
import com.xmedia.social.youtube.entity.YouTubeSearchData;
import com.xmedia.social.youtube.entity.YouTubeSearchId;
import com.xmedia.social.youtube.entity.YouTubeSearchItem;
import com.xmedia.social.youtube.entity.YouTubeSearchSnippet;
import com.xmedia.social.youtube.entity.YouTubeStatistics;
import com.xmedia.social.youtube.entity.YouTubeThumbnail;
import com.xmedia.social.youtube.entity.YouTubeThumbnailDetails;
import com.xmedia.social.youtube.entity.YouTubeToken;
import com.xmedia.social.youtube.entity.YouTubeTopLevelComment;
import com.xmedia.social.youtube.repository.YouTubeChannelDataRepository;
import com.xmedia.social.youtube.repository.YouTubeCommentThreadDataRepository;
import com.xmedia.social.youtube.repository.YouTubeSearchDataRepository;
import com.xmedia.social.youtube.repository.YouTubeTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    @Value("${google.oauth2.redirect-uri}")
    private String redirectUri;

    private final YouTubeOAuth2Client youTubeOAuth2Client;
    private final YouTubeTokenRepository youTubeTokenRepository;
    private final YouTubeDataApiClient youTubeDataApiClient;
    private final YouTubeChannelDataRepository youTubeChannelDataRepository;
    private final YouTubeSearchDataRepository youTubeSearchDataRepository;
    private final YouTubeCommentThreadDataRepository youTubeCommentThreadDataRepository;
    // private final YouTubeCommentThreadItemDataRepository youTubeCommentThreadItemDataRepository;
    // private final ObjectMapper objectMapper;

    public YouTubeToken handleOAuth2Callback(String authorizationCode, String scope) {
        // Save the initial authorization code and scope
        YouTubeToken youTubeToken = YouTubeToken.builder()
                .authorizationCode(authorizationCode)
                .scope(scope)
                .build();
        youTubeTokenRepository.save(youTubeToken);

        // Exchange authorization code for access token
        GoogleTokenResponse tokenResponse = youTubeOAuth2Client.getAccessToken(
                clientId,
                clientSecret,
                authorizationCode,
                "authorization_code",
                redirectUri
        );

        // Update the stored token with access and refresh tokens and calculate expiry
        youTubeToken.setAccessToken(tokenResponse.getAccessToken());
        youTubeToken.setRefreshToken(tokenResponse.getRefreshToken());
        youTubeToken.setExpiresIn(tokenResponse.getExpiresIn());
        youTubeToken.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn()));
        // Set refresh token expiry for 60 days from creation, if a refresh token is provided
        if (tokenResponse.getRefreshToken() != null) {
            youTubeToken.setRefreshTokenExpiresAt(LocalDateTime.now().plusDays(60));
        }
        return youTubeTokenRepository.save(youTubeToken);
    }

    private YouTubeToken refreshAccessToken(YouTubeToken youTubeToken) {
        if (youTubeToken.getRefreshToken() == null || youTubeToken.getRefreshTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token is expired or not available. Please re-authorize.");
        }
        GoogleTokenResponse tokenResponse = youTubeOAuth2Client.refreshAccessToken(
                clientId,
                clientSecret,
                youTubeToken.getRefreshToken(),
                "refresh_token"
        );
        youTubeToken.setAccessToken(tokenResponse.getAccessToken());
        youTubeToken.setExpiresIn(tokenResponse.getExpiresIn());
        youTubeToken.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn()));
        return youTubeTokenRepository.save(youTubeToken);
    }

    private YouTubeToken getYouTubeToken(Long youtubeTokenId) {
        return youTubeTokenRepository.findById(youtubeTokenId)
                .orElseThrow(() -> new RuntimeException("YouTube token not found for ID: " + youtubeTokenId));
    }

    private String getValidAccessToken(YouTubeToken youTubeToken) {
        if (youTubeToken.getAccessToken() == null || youTubeToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            // Access token is null or expired, try to refresh
            youTubeToken = refreshAccessToken(youTubeToken);
        }
        return youTubeToken.getAccessToken();
    }

    public YouTubeChannelListResponse getMyChannelDetails(Long youtubeTokenId) {
        YouTubeToken youTubeToken = getYouTubeToken(youtubeTokenId);
        String accessToken = getValidAccessToken(youTubeToken);
        YouTubeChannelListResponse channelDetails = youTubeDataApiClient.getMyChannelDetails("Bearer " + accessToken, "snippet,contentDetails,statistics", true);

        // Map the YouTubeChannelListResponse to YouTubeChannelData entity
        YouTubeChannelDataEntity youTubeChannelData = YouTubeChannelDataEntity.builder()
                .youTubeToken(youTubeToken)
                .kind(channelDetails.getKind())
                .etag(channelDetails.getEtag())
                .pageInfo(YouTubePageInfo.builder()
                        .totalResults(channelDetails.getPageInfo().getTotalResults())
                        .resultsPerPage(channelDetails.getPageInfo().getResultsPerPage())
                        .build())
                .items(channelDetails.getItems().stream().map(itemDto -> {
                    YouTubeChannelItemEntity item = YouTubeChannelItemEntity.builder()
                            .kind(itemDto.getKind())
                            .etag(itemDto.getEtag())
                            .youtubeChannelId(itemDto.getId())
                            .snippet(YouTubeChannelSnippetEntity.builder()
                                    .title(itemDto.getSnippet().getTitle())
                                    .description(itemDto.getSnippet().getDescription())
                                    .customUrl(itemDto.getSnippet().getCustomUrl())
                                    .publishedAt(itemDto.getSnippet().getPublishedAt())
                                    .thumbnails(YouTubeThumbnailDetails.builder()
                                            .defaultThumbnail(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getHeight())
                                                    .build())
                                            .medium(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getMedium().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getMedium().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getMedium().getHeight())
                                                    .build())
                                            .high(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getHigh().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getHigh().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getHigh().getHeight())
                                                    .build())
                                            .build())
                                    .localized(YouTubeLocalized.builder()
                                            .title(itemDto.getSnippet().getLocalized().getTitle())
                                            .description(itemDto.getSnippet().getLocalized().getDescription())
                                            .build())
                                    .build())
                            .contentDetails(YouTubeContentDetailsEntity.builder()
                                    .relatedPlaylists(YouTubeRelatedPlaylists.builder()
                                            .likes(itemDto.getContentDetails().getRelatedPlaylists().getLikes())
                                            .uploads(itemDto.getContentDetails().getRelatedPlaylists().getUploads())
                                            .build())
                                    .build())
                            .statistics(YouTubeStatistics.builder()
                                    .viewCount(itemDto.getStatistics().getViewCount())
                                    .subscriberCount(itemDto.getStatistics().getSubscriberCount())
                                    .hiddenSubscriberCount(itemDto.getStatistics().isHiddenSubscriberCount())
                                    .videoCount(itemDto.getStatistics().getVideoCount())
                                    .build())
                            .youTubeChannelData(null) // Temporarily null, will be set after parent save
                            .build();
                    return item;
                }).collect(Collectors.toList())) // Collect to a list here
                .build();

        // Save the YouTubeChannelData entity
        youTubeChannelDataRepository.save(youTubeChannelData);

        // Set the parent relationship for each item after the parent is saved
        youTubeChannelData.getItems().forEach(item -> {
            item.setYouTubeChannelData(youTubeChannelData);
        });

        return channelDetails;
    }

    public YouTubeSearchListResponse searchYouTube(Long youtubeTokenId, String channelId, int maxResults, String order) {
        YouTubeToken youTubeToken = getYouTubeToken(youtubeTokenId);
        String accessToken = getValidAccessToken(youTubeToken);
        YouTubeSearchListResponse searchResults = youTubeDataApiClient.search("Bearer " + accessToken, "snippet", channelId, maxResults, order);

        // Map the YouTubeSearchListResponse to YouTubeSearchData entity
        YouTubeSearchData youTubeSearchData = YouTubeSearchData.builder()
                .youTubeToken(youTubeToken)
                .kind(searchResults.getKind())
                .etag(searchResults.getEtag())
                .regionCode(searchResults.getRegionCode())
                .pageInfo(YouTubePageInfo.builder()
                        .totalResults(searchResults.getPageInfo().getTotalResults())
                        .resultsPerPage(searchResults.getPageInfo().getResultsPerPage())
                        .build())
                .items(searchResults.getItems().stream().map(itemDto -> {
                    YouTubeSearchItem item = YouTubeSearchItem.builder()
                            .kind(itemDto.getKind())
                            .etag(itemDto.getEtag())
                            .searchId(YouTubeSearchId.builder()
                                    .kind(itemDto.getId().getKind())
                                    .videoId(itemDto.getId().getVideoId())
                                    .channelId(itemDto.getId().getChannelId())
                                    .playlistId(itemDto.getId().getPlaylistId())
                                    .build())
                            .snippet(YouTubeSearchSnippet.builder()
                                    .publishedAt(itemDto.getSnippet().getPublishedAt())
                                    .channelId(itemDto.getSnippet().getChannelId())
                                    .title(itemDto.getSnippet().getTitle())
                                    .description(itemDto.getSnippet().getDescription())
                                    .thumbnails(YouTubeThumbnailDetails.builder()
                                            .defaultThumbnail(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getDefaultThumbnail().getHeight())
                                                    .build())
                                            .medium(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getMedium().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getMedium().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getMedium().getHeight())
                                                    .build())
                                            .high(YouTubeThumbnail.builder()
                                                    .url(itemDto.getSnippet().getThumbnails().getHigh().getUrl())
                                                    .width(itemDto.getSnippet().getThumbnails().getHigh().getWidth())
                                                    .height(itemDto.getSnippet().getThumbnails().getHigh().getHeight())
                                                    .build())
                                            .build())
                                    .channelTitle(itemDto.getSnippet().getChannelTitle())
                                    .liveBroadcastContent(itemDto.getSnippet().getLiveBroadcastContent())
                                    .publishTime(itemDto.getSnippet().getPublishTime())
                                    .build())
                            .youTubeSearchData(null) // Temporarily null, will be set after parent save
                            .build();
                    return item;
                }).collect(Collectors.toList())) // Collect to a list here
                .build();

        // Save the YouTubeSearchData entity
        youTubeSearchDataRepository.save(youTubeSearchData);

        // Set the parent relationship for each item after the parent is saved
        youTubeSearchData.getItems().forEach(item -> {
            item.setYouTubeSearchData(youTubeSearchData);
        });

        return searchResults;
    }

    public YouTubeCommentThreadListResponse getCommentThreads(Long youtubeTokenId, String videoId, int maxResults) {
        YouTubeToken youTubeToken = getYouTubeToken(youtubeTokenId);
        String accessToken = getValidAccessToken(youTubeToken);
        YouTubeCommentThreadListResponse commentThreads = youTubeDataApiClient.getCommentThreads("Bearer " + accessToken, "snippet,replies", videoId, maxResults);

        // Map the YouTubeCommentThreadListResponse to YouTubeCommentThreadData entity
        YouTubeCommentThreadDataEntity youTubeCommentThreadData = YouTubeCommentThreadDataEntity.builder()
                .youTubeToken(youTubeToken)
                .kind(commentThreads.getKind())
                .etag(commentThreads.getEtag())
                .pageInfo(YouTubePageInfo.builder()
                        .totalResults(commentThreads.getPageInfo().getTotalResults())
                        .resultsPerPage(commentThreads.getPageInfo().getResultsPerPage())
                        .build())
                .items(commentThreads.getItems().stream().map(itemDto -> {
                    YouTubeCommentThreadItemEntity item = YouTubeCommentThreadItemEntity.builder()
                            .kind(itemDto.getKind())
                            .etag(itemDto.getEtag())
                            .youtubeCommentThreadItemId(itemDto.getId())
                            .snippet(YouTubeCommentThreadSnippetEntity.builder()
                                    .channelId(itemDto.getSnippet().getChannelId())
                                    .videoId(itemDto.getSnippet().getVideoId())
                                    .topLevelComment(YouTubeTopLevelComment.builder()
                                            .kind(itemDto.getSnippet().getTopLevelComment().getKind())
                                            .etag(itemDto.getSnippet().getTopLevelComment().getEtag())
                                            .youtubeCommentId(itemDto.getSnippet().getTopLevelComment().getId())
                                            .snippet(YouTubeCommentSnippetEntity.builder()
                                                    .channelId(itemDto.getSnippet().getTopLevelComment().getSnippet().getChannelId())
                                                    .videoId(itemDto.getSnippet().getTopLevelComment().getSnippet().getVideoId())
                                                    .textDisplay(itemDto.getSnippet().getTopLevelComment().getSnippet().getTextDisplay())
                                                    .textOriginal(itemDto.getSnippet().getTopLevelComment().getSnippet().getTextOriginal())
                                                    .authorDisplayName(itemDto.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName())
                                                    .authorProfileImageUrl(itemDto.getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl())
                                                    .authorChannelUrl(itemDto.getSnippet().getTopLevelComment().getSnippet().getAuthorChannelUrl())
                                                    .authorChannelId(YouTubeAuthorChannelIdEntity.builder()
                                                            .value(itemDto.getSnippet().getTopLevelComment().getSnippet().getAuthorChannelId().getValue())
                                                            .build())
                                                    .canRate(itemDto.getSnippet().getTopLevelComment().getSnippet().isCanRate())
                                                    .viewerRating(itemDto.getSnippet().getTopLevelComment().getSnippet().getViewerRating())
                                                    .likeCount(itemDto.getSnippet().getTopLevelComment().getSnippet().getLikeCount())
                                                    .publishedAt(itemDto.getSnippet().getTopLevelComment().getSnippet().getPublishedAt())
                                                    .updatedAt(itemDto.getSnippet().getTopLevelComment().getSnippet().getUpdatedAt())
                                                    .build())
                                            .build())
                                    .canReply(itemDto.getSnippet().isCanReply())
                                    .totalReplyCount(itemDto.getSnippet().getTotalReplyCount())
                                    .isPublic(itemDto.getSnippet().isPublic())
                                    .build())
                            .replies(itemDto.getReplies() != null ? YouTubeReplies.builder()
                                    .comments(itemDto.getReplies().getComments().stream().map(commentDto -> {
                                        YouTubeCommentEntity comment = YouTubeCommentEntity.builder()
                                                .kind(commentDto.getKind())
                                                .etag(commentDto.getEtag())
                                                .youtubeCommentId(commentDto.getId())
                                                .snippet(YouTubeCommentSnippetEntity.builder()
                                                        .channelId(commentDto.getSnippet().getChannelId())
                                                        .videoId(commentDto.getSnippet().getVideoId())
                                                        .textDisplay(commentDto.getSnippet().getTextDisplay())
                                                        .textOriginal(commentDto.getSnippet().getTextOriginal())
                                                        .authorDisplayName(commentDto.getSnippet().getAuthorDisplayName())
                                                        .authorProfileImageUrl(commentDto.getSnippet().getAuthorProfileImageUrl())
                                                        .authorChannelUrl(commentDto.getSnippet().getAuthorChannelUrl())
                                                        .authorChannelId(YouTubeAuthorChannelIdEntity.builder()
                                                                .value(commentDto.getSnippet().getAuthorChannelId().getValue())
                                                                .build())
                                                        .canRate(commentDto.getSnippet().isCanRate())
                                                        .viewerRating(commentDto.getSnippet().getViewerRating())
                                                        .likeCount(commentDto.getSnippet().getLikeCount())
                                                        .publishedAt(commentDto.getSnippet().getPublishedAt())
                                                        .updatedAt(commentDto.getSnippet().getUpdatedAt())
                                                        .build())
                                                .youTubeReplies(null) // Temporarily null, will be set after parent save
                                                .build();
                                        return comment;
                                    }).collect(Collectors.toList()))
                                    .build() : null)
                            .youTubeCommentThreadData(null) // Temporarily null, will be set after parent save
                            .build();
                    return item;
                }).collect(Collectors.toList()))
                .build();

        // Save the YouTubeCommentThreadData entity
        youTubeCommentThreadDataRepository.save(youTubeCommentThreadData);

        // Set the parent relationship for each item and their replies after the parent is saved
        youTubeCommentThreadData.getItems().forEach(item -> {
            item.setYouTubeCommentThreadData(youTubeCommentThreadData);
            if (item.getReplies() != null) {
                item.getReplies().getComments().forEach(comment -> {
                    comment.setYouTubeReplies(item.getReplies());
                });
            }
        });

        return commentThreads;
    }
}
