package com.xmedia.social.youtube.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmedia.social.feign.client.YouTubeDataApiClient;
import com.xmedia.social.feign.client.YouTubeTokenFeignClient;
import com.xmedia.social.youtube.dto.AuthorChannelId;
import com.xmedia.social.youtube.dto.CommentSnippet;
import com.xmedia.social.youtube.dto.CommentThreadItem;
import com.xmedia.social.youtube.dto.CommentThreadSnippet;
import com.xmedia.social.youtube.dto.ContentDetails;
import com.xmedia.social.youtube.dto.Item;
import com.xmedia.social.youtube.dto.Localized;
import com.xmedia.social.youtube.dto.ResourceId;
import com.xmedia.social.youtube.dto.SearchResultItem;
import com.xmedia.social.youtube.dto.SearchSnippet;
import com.xmedia.social.youtube.dto.Snippet;
import com.xmedia.social.youtube.dto.Statistics;
import com.xmedia.social.youtube.dto.Thumbnail;
import com.xmedia.social.youtube.dto.TopLevelComment;
import com.xmedia.social.youtube.dto.VideoCommentsResponse;
import com.xmedia.social.youtube.dto.YouTubeChannelDetailsResponse;
import com.xmedia.social.youtube.dto.YouTubeChannelListResponse;
import com.xmedia.social.youtube.dto.YouTubeCommentThreadListResponse;
import com.xmedia.social.youtube.dto.YouTubeSearchListResponse;
import com.xmedia.social.youtube.dto.YouTubeTokenResponse;
import com.xmedia.social.youtube.entity.YouTubeSearchDataEntity;
import com.xmedia.social.youtube.model.YouTubeAuthorChannelId;
import com.xmedia.social.youtube.model.YouTubeChannel;
import com.xmedia.social.youtube.model.YouTubeChannelSnippetEntity;
import com.xmedia.social.youtube.model.YouTubeComment;
import com.xmedia.social.youtube.model.YouTubeCommentThread;
import com.xmedia.social.youtube.model.YouTubeCredential;
import com.xmedia.social.youtube.model.YouTubeLocalized;
import com.xmedia.social.youtube.model.YouTubeResourceId;
import com.xmedia.social.youtube.model.YouTubeSearchResult;
import com.xmedia.social.youtube.model.YouTubeSearchSnippet;
import com.xmedia.social.youtube.model.YouTubeThumbnail;
import com.xmedia.social.youtube.repository.YouTubeAuthorChannelIdRepository;
import com.xmedia.social.youtube.repository.YouTubeChannelRepository;
import com.xmedia.social.youtube.repository.YouTubeChannelSnippetEntityRepository;
import com.xmedia.social.youtube.repository.YouTubeCommentRepository;
import com.xmedia.social.youtube.repository.YouTubeCommentThreadRepository;
import com.xmedia.social.youtube.repository.YouTubeCredentialRepository;
import com.xmedia.social.youtube.repository.YouTubeLocalizedRepository;
import com.xmedia.social.youtube.repository.YouTubeResourceIdRepository;
import com.xmedia.social.youtube.repository.YouTubeSearchResultRepository;
import com.xmedia.social.youtube.repository.YouTubeSearchSnippetRepository;
import com.xmedia.social.youtube.repository.YouTubeThumbnailRepository;
import com.xmedia.social.youtube.repository.YouTubeVideoRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YouTubeService {

	@Value("${youtube.oauth2.client-id}")
	private String clientId;

	@Value("${youtube.oauth2.client-secret}")
	private String clientSecret;

	@Value("${youtube.oauth2.redirect-uri}")
	private String redirectUri;

	private final YouTubeTokenFeignClient youtubeTokenFeignClient;
	private final YouTubeCredentialRepository youtubeCredentialRepository;
	private final YouTubeDataApiClient youTubeDataApiClient;
	private final YouTubeChannelRepository youTubeChannelRepository;
	private final YouTubeCommentThreadRepository youTubeCommentThreadRepository;
	private final YouTubeCommentRepository youTubeCommentRepository;
	private final YouTubeAuthorChannelIdRepository youTubeAuthorChannelIdRepository;
	private final YouTubeSearchResultRepository youTubeSearchResultRepository;
	private final YouTubeResourceIdRepository youTubeResourceIdRepository;
	private final YouTubeSearchSnippetRepository youTubeSearchSnippetRepository;
	private final YouTubeThumbnailRepository youTubeThumbnailRepository;
	private final YouTubeChannelSnippetEntityRepository youTubeChannelSnippetEntityRepository;
	private final YouTubeLocalizedRepository youTubeLocalizedRepository;
	private final YouTubeVideoRepository youTubeVideoRepository;

	private YouTubeCredential getYouTubeCredential() {
		return youtubeCredentialRepository.findAll().stream().findFirst().orElse(null);
	}

	private String getAccessToken() {
		YouTubeCredential credential = getYouTubeCredential();
		if (credential != null) {
			if (credential.getIssuedAt() == null) {
				credential.setIssuedAt(System.currentTimeMillis());
				youtubeCredentialRepository.save(credential);
			}
			// Check if token is expired (add your own logic for expiry)
			// For simplicity, let's assume it expires after a certain time, e.g., 1 hour
			// You might want to store timestamp of token creation for accurate expiry
			// checks
			if (isAccessTokenExpired(credential)) {
				// Refresh token
				YouTubeTokenResponse refreshedToken = youtubeTokenFeignClient.refreshAccessToken(clientId, clientSecret,
						credential.getRefreshToken(), "refresh_token");
				// Update and save new credential
				credential.setAccessToken(refreshedToken.getAccessToken());
				credential.setExpiresIn(refreshedToken.getExpiresIn());
				youtubeCredentialRepository.save(credential);
				return refreshedToken.getAccessToken();
			} else {
				return credential.getAccessToken();
			}
		}
		return null; // No credential found
	}

	private boolean isAccessTokenExpired(YouTubeCredential credential) {
		if (credential.getIssuedAt() == null) {
			return true; // If issuedAt is null, assume token is expired to force a refresh
		}
		long issuedAt = credential.getIssuedAt();
		long expiresInMillis = credential.getExpiresIn() * 1000L; // expiresIn is in seconds
		long currentTime = System.currentTimeMillis();

		// Consider a small buffer before actual expiry, e.g., 5 minutes
		long bufferMillis = 5 * 60 * 1000L;

		return (currentTime - issuedAt) >= (expiresInMillis - bufferMillis);
	}

	public YouTubeTokenResponse exchangeCodeForTokens(String authorizationCode) {
		YouTubeTokenResponse tokenResponse = youtubeTokenFeignClient.exchangeAuthorizationCodeForTokens(clientId,
				clientSecret, authorizationCode, "authorization_code", redirectUri);
		saveYouTubeCredential(tokenResponse);
		return tokenResponse;
	}

	private void saveYouTubeCredential(YouTubeTokenResponse tokenResponse) {
		YouTubeCredential youTubeCredential = YouTubeCredential.builder().accessToken(tokenResponse.getAccessToken())
				.expiresIn(tokenResponse.getExpiresIn()).scope(tokenResponse.getScope())
				.tokenType(tokenResponse.getTokenType()).refreshToken(tokenResponse.getRefreshToken())
				.issuedAt(System.currentTimeMillis()).build();
		youtubeCredentialRepository.save(youTubeCredential);
	}

	@Transactional
	public YouTubeChannelDetailsResponse getAndSaveMyChannelDetails(String accessToken) {

		String currentAccessToken = getAccessToken();
		if (currentAccessToken == null) {
			return null;
		}

		YouTubeChannelListResponse channelListResponse;

		try {
			channelListResponse = youTubeDataApiClient.getMyChannelDetails("Bearer " + currentAccessToken,
					"snippet,contentDetails,statistics", true);
		} catch (FeignException e) {
			System.err.println("Error fetching channel details: " + e.getMessage());
			return null;
		}

		if (channelListResponse.getItems() == null || channelListResponse.getItems().isEmpty()) {
			return null;
		}

		Item originalItem = channelListResponse.getItems().get(0);
		Snippet snippet = originalItem.getSnippet();
		ContentDetails contentDetails = originalItem.getContentDetails();
		Statistics statistics = originalItem.getStatistics();
		Localized localized = snippet.getLocalized();

		// --------------------------
		// SAVE / UPDATE THUMBNAILS
		// --------------------------
		YouTubeThumbnail defaultThumb = saveOrUpdateThumbnail(
				snippet.getThumbnails() != null ? snippet.getThumbnails().getDefaultThumbnail() : null);
		YouTubeThumbnail mediumThumb = saveOrUpdateThumbnail(
				snippet.getThumbnails() != null ? snippet.getThumbnails().getMedium() : null);
		YouTubeThumbnail highThumb = saveOrUpdateThumbnail(
				snippet.getThumbnails() != null ? snippet.getThumbnails().getHigh() : null);

		// --------------------------
		// LOCALIZED DETAILS
		// --------------------------
		final YouTubeLocalized youTubeLocalized = (localized != null) ? youTubeLocalizedRepository.save(
				YouTubeLocalized.builder().title(localized.getTitle()).description(localized.getDescription()).build())
				: null;

		// --------------------------
		// CHANNEL SNIPPET ENTITY
		// --------------------------
		YouTubeChannelSnippetEntity snippetEntity = youTubeChannelSnippetEntityRepository
				.findByDefaultThumbnailUrl(defaultThumb != null ? defaultThumb.getUrl() : null).map(existing -> {
					existing.setTitle(snippet.getTitle());
					existing.setDescription(snippet.getDescription());
					existing.setCustomUrl(snippet.getCustomUrl());
					existing.setPublishedAt(snippet.getPublishedAt());
					existing.setDefaultThumbnail(defaultThumb);
					existing.setMediumThumbnail(mediumThumb);
					existing.setHighThumbnail(highThumb);
					existing.setLocalized(youTubeLocalized);
					return existing;
				})
				.orElseGet(() -> YouTubeChannelSnippetEntity.builder().title(snippet.getTitle())
						.description(snippet.getDescription()).customUrl(snippet.getCustomUrl())
						.publishedAt(snippet.getPublishedAt()).defaultThumbnail(defaultThumb)
						.mediumThumbnail(mediumThumb).highThumbnail(highThumb).localized(youTubeLocalized).build());

		youTubeChannelSnippetEntityRepository.save(snippetEntity);

		// --------------------------
		// SAVE / UPDATE CHANNEL
		// --------------------------
		YouTubeChannel channel = youTubeChannelRepository.findById(originalItem.getId()).map(existing -> {
			existing.setSnippet(snippetEntity);
			existing.setLikesPlaylistId(contentDetails.getRelatedPlaylists().getLikes());
			existing.setUploadsPlaylistId(contentDetails.getRelatedPlaylists().getUploads());
			existing.setViewCount(statistics.getViewCount() == null ? 0L : Long.parseLong(statistics.getViewCount()));
			existing.setSubscriberCount(
					statistics.getSubscriberCount() == null ? 0L : Long.parseLong(statistics.getSubscriberCount()));
			existing.setHiddenSubscriberCount(statistics.getHiddenSubscriberCount());
			existing.setVideoCount(
					statistics.getVideoCount() == null ? 0L : Long.parseLong(statistics.getVideoCount()));
			return existing;
		}).orElseGet(() -> YouTubeChannel.builder().id(originalItem.getId()).snippet(snippetEntity)
				.likesPlaylistId(contentDetails.getRelatedPlaylists().getLikes())
				.uploadsPlaylistId(contentDetails.getRelatedPlaylists().getUploads())
				.viewCount(statistics.getViewCount() == null ? 0L : Long.parseLong(statistics.getViewCount()))
				.subscriberCount(
						statistics.getSubscriberCount() == null ? 0L : Long.parseLong(statistics.getSubscriberCount()))
				.hiddenSubscriberCount(statistics.getHiddenSubscriberCount())
				.videoCount(statistics.getVideoCount() == null ? 0L : Long.parseLong(statistics.getVideoCount()))
				.build());

		youTubeChannelRepository.save(channel);

		// --------------------------
		// BUILD RESPONSE
		// --------------------------
		Item responseItem = Item.builder().kind("youtube#channel").etag(originalItem.getEtag()).id(originalItem.getId())
				.snippet(originalItem.getSnippet()).contentDetails(originalItem.getContentDetails())
				.statistics(originalItem.getStatistics()).build();

		return YouTubeChannelDetailsResponse.builder().kind("youtube#channelListResponse")
				.etag(channelListResponse.getEtag()).pageInfo(channelListResponse.getPageInfo())
				.items(List.of(responseItem)).build();
	}

	private YouTubeThumbnail saveOrUpdateThumbnail(Thumbnail apiThumbnail) {
		if (apiThumbnail == null)
			return null;

		YouTubeThumbnail thumb = YouTubeThumbnail.builder().url(apiThumbnail.getUrl()).width(apiThumbnail.getWidth())
				.height(apiThumbnail.getHeight()).build();

		return youTubeThumbnailRepository.findById(thumb.getUrl()).map(existing -> {
			existing.setWidth(thumb.getWidth());
			existing.setHeight(thumb.getHeight());
			return existing;
		}).orElseGet(() -> youTubeThumbnailRepository.save(thumb));
	}

	// Helper method to save or fetch existing AuthorChannelId
	private YouTubeAuthorChannelId saveAuthorChannelId(AuthorChannelId originalAuthorChannelId) {
		if (originalAuthorChannelId == null)
			return null;

		return youTubeAuthorChannelIdRepository.findByValue(originalAuthorChannelId.getValue()).orElseGet(() -> {
			YouTubeAuthorChannelId entity = YouTubeAuthorChannelId.builder().value(originalAuthorChannelId.getValue())
					.build();
			return youTubeAuthorChannelIdRepository.save(entity);
		});
	}

	public YouTubeCommentThreadListResponse getAndSaveCommentThreads(String accessToken, String videoId,
			int maxResults) {
		String currentAccessToken = getAccessToken();
		if (currentAccessToken == null) {
			return null; // No valid access token
		}

		YouTubeCommentThreadListResponse commentThreadListResponse;
		try {
			commentThreadListResponse = youTubeDataApiClient.getCommentThreads("Bearer " + currentAccessToken,
					"snippet,replies", videoId, maxResults);
		} catch (FeignException.Unauthorized e) {
			System.err.println("Unauthorized access: " + e.getMessage());
			return null;
		} catch (FeignException e) {
			System.err.println("Error fetching comment threads: " + e.getMessage());
			return null;
		}
		if (commentThreadListResponse.getItems() != null && !commentThreadListResponse.getItems().isEmpty()) {
			for (CommentThreadItem originalItem : commentThreadListResponse.getItems()) {
				CommentThreadSnippet originalSnippet = originalItem.getSnippet();
				TopLevelComment originalTopLevelComment = originalSnippet.getTopLevelComment();
				CommentSnippet originalCommentSnippet = originalTopLevelComment.getSnippet();
				AuthorChannelId originalAuthorChannelId = originalCommentSnippet.getAuthorChannelId();

				// Save or get existing AuthorChannelId
				YouTubeAuthorChannelId youTubeAuthorChannelId = saveAuthorChannelId(originalAuthorChannelId);

				// Save or update YouTubeComment safely
				YouTubeComment commentToSave = YouTubeComment.builder().id(originalTopLevelComment.getId())
						.kind(originalTopLevelComment.getKind()).etag(originalTopLevelComment.getEtag())
						.channelId(originalCommentSnippet.getChannelId()).videoId(originalCommentSnippet.getVideoId())
						.textDisplay(originalCommentSnippet.getTextDisplay())
						.textOriginal(originalCommentSnippet.getTextOriginal())
						.authorDisplayName(originalCommentSnippet.getAuthorDisplayName())
						.authorProfileImageUrl(originalCommentSnippet.getAuthorProfileImageUrl())
						.authorChannelUrl(originalCommentSnippet.getAuthorChannelUrl())
						.authorChannelId(youTubeAuthorChannelId).canRate(originalCommentSnippet.getCanRate())
						.viewerRating(originalCommentSnippet.getViewerRating())
						.likeCount(originalCommentSnippet.getLikeCount())
						.publishedAt(originalCommentSnippet.getPublishedAt())
						.updatedAt(originalCommentSnippet.getUpdatedAt()).build();

				YouTubeComment savedComment = youTubeCommentRepository
						.findByAuthorChannelIdValue(originalCommentSnippet.getChannelId()).map(existing -> {
							existing.setTextDisplay(commentToSave.getTextDisplay());
							existing.setTextOriginal(commentToSave.getTextOriginal());
							existing.setAuthorDisplayName(commentToSave.getAuthorDisplayName());
							existing.setAuthorProfileImageUrl(commentToSave.getAuthorProfileImageUrl());
							existing.setAuthorChannelUrl(commentToSave.getAuthorChannelUrl());
							existing.setCanRate(commentToSave.getCanRate());
							existing.setViewerRating(commentToSave.getViewerRating());
							existing.setLikeCount(commentToSave.getLikeCount());
							existing.setPublishedAt(commentToSave.getPublishedAt());
							existing.setUpdatedAt(commentToSave.getUpdatedAt());
							return youTubeCommentRepository.save(existing);
						}).orElseGet(() -> youTubeCommentRepository.save(commentToSave));

				// Save or update YouTubeCommentThread safely
				YouTubeCommentThread threadToSave = YouTubeCommentThread.builder().id(originalItem.getId())
						.kind(originalItem.getKind()).etag(originalItem.getEtag())
						.channelId(originalSnippet.getChannelId()).videoId(originalSnippet.getVideoId())
						.topLevelComment(savedComment).canReply(originalSnippet.getCanReply())
						.totalReplyCount(originalSnippet.getTotalReplyCount()).isPublic(originalSnippet.getIsPublic())
						.build();

				youTubeCommentThreadRepository.findByTopLevelComment_Id(savedComment.getId()).map(existingThread -> {
					// Update existing thread
					existingThread.setCanReply(threadToSave.getCanReply());
					existingThread.setTotalReplyCount(threadToSave.getTotalReplyCount());
					existingThread.setIsPublic(threadToSave.getIsPublic());
					existingThread.setEtag(threadToSave.getEtag());
					existingThread.setKind(threadToSave.getKind());
					return youTubeCommentThreadRepository.save(existingThread);
				}).orElseGet(() -> youTubeCommentThreadRepository.save(threadToSave));
			}
		}

		return commentThreadListResponse;
	}

	public YouTubeSearchListResponse getAndSaveSearchResults(String accessToken, String channelId, int maxResults,
			String order) {
		String currentAccessToken = getAccessToken();
		if (currentAccessToken == null)
			return null;

		YouTubeSearchListResponse searchListResponse;
		try {
			searchListResponse = youTubeDataApiClient.search("Bearer " + currentAccessToken, "snippet", channelId,
					maxResults, order);
		} catch (FeignException.Unauthorized e) {
			System.err.println("Unauthorized access: " + e.getMessage());
			return null;
		} catch (FeignException e) {
			System.err.println("Error fetching search results: " + e.getMessage());
			return null;
		}

		if (searchListResponse.getItems() != null && !searchListResponse.getItems().isEmpty()) {
			for (SearchResultItem originalItem : searchListResponse.getItems()) {
				ResourceId originalResourceId = originalItem.getId();
				SearchSnippet originalSnippet = originalItem.getSnippet();

				// Save thumbnails
				YouTubeThumbnail defaultThumbnail = saveThumbnail(
						originalSnippet.getThumbnails() != null ? originalSnippet.getThumbnails().getDefaultThumbnail()
								: null);
				YouTubeThumbnail mediumThumbnail = saveThumbnail(
						originalSnippet.getThumbnails() != null ? originalSnippet.getThumbnails().getMedium() : null);
				YouTubeThumbnail highThumbnail = saveThumbnail(
						originalSnippet.getThumbnails() != null ? originalSnippet.getThumbnails().getHigh() : null);

				// Save snippet
				YouTubeSearchSnippet youTubeSearchSnippet = YouTubeSearchSnippet.builder()
						.id(UUID.randomUUID().toString()).publishedAt(originalSnippet.getPublishedAt())
						.channelId(originalSnippet.getChannelId()).title(originalSnippet.getTitle())
						.description(originalSnippet.getDescription()).defaultThumbnail(defaultThumbnail)
						.mediumThumbnail(mediumThumbnail).highThumbnail(highThumbnail)
						.channelTitle(originalSnippet.getChannelTitle())
						.liveBroadcastContent(originalSnippet.getLiveBroadcastContent())
						.publishTime(originalSnippet.getPublishTime()).build();
				youTubeSearchSnippetRepository.save(youTubeSearchSnippet);

				// Save ResourceId safely
				YouTubeResourceId youTubeResourceId = saveResourceId(originalResourceId);

				// Save search result
				YouTubeSearchResult youTubeSearchResult = YouTubeSearchResult.builder().id(UUID.randomUUID().toString())
						.kind(originalItem.getKind()).etag(originalItem.getEtag()).resourceId(youTubeResourceId)
						.snippet(youTubeSearchSnippet).build();
				youTubeSearchResultRepository.save(youTubeSearchResult);

				searchListResponse.getItems().stream()
						.filter(item -> item.getId() != null && item.getId().getVideoId() != null).forEach(item -> {

							String videoId = item.getId().getVideoId();

							// if already exists → skip
							if (youTubeVideoRepository.findByVideoId(videoId).isPresent()) {
								return;
							}

							YouTubeSearchDataEntity entity = YouTubeSearchDataEntity.builder()
									.videoId(item.getId().getVideoId()).channelId(item.getSnippet().getChannelId())
									.build();

							youTubeVideoRepository.save(entity);
						});

			}
		}

		return searchListResponse;
	}

	// Save thumbnail safely
	private YouTubeThumbnail saveThumbnail(Thumbnail thumb) {
		if (thumb == null)
			return null;

		return youTubeThumbnailRepository.findById(thumb.getUrl())
				.orElseGet(() -> youTubeThumbnailRepository.save(YouTubeThumbnail.builder().url(thumb.getUrl())
						.width(thumb.getWidth()).height(thumb.getHeight()).build()));
	}

	// Save ResourceId safely
	private YouTubeResourceId saveResourceId(ResourceId resourceId) {
		if (resourceId == null)
			return null;

		return youTubeResourceIdRepository.findByKind(resourceId.getKind()).orElseGet(() -> {
			YouTubeResourceId entity = YouTubeResourceId.builder().id(UUID.randomUUID().toString())
					.kind(resourceId.getKind()).videoId(resourceId.getVideoId()).channelId(resourceId.getChannelId())
					.playlistId(resourceId.getPlaylistId()).build();
			return youTubeResourceIdRepository.save(entity);
		});
	}

	public List<VideoCommentsResponse> getAllVideoComments(String accessToken) {

		List<YouTubeSearchDataEntity> videos = youTubeVideoRepository.findAll();

		List<VideoCommentsResponse> finalResponse = new ArrayList<>();

		for (YouTubeSearchDataEntity video : videos) {

			String videoId = video.getVideoId();

			YouTubeCommentThreadListResponse response = getAndSaveCommentThreads(accessToken, videoId, 50);

			if (response != null && response.getItems() != null) {
				finalResponse.add(new VideoCommentsResponse(videoId, response.getItems()));
			}
		}

		return finalResponse;
	}

}
