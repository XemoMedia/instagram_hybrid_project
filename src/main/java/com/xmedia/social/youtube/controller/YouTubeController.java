package com.xmedia.social.youtube.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.youtube.dto.AuthorizationUrlResponse;
import com.xmedia.social.youtube.dto.VideoCommentsResponse;
import com.xmedia.social.youtube.dto.YouTubeChannelDetailsResponse;
import com.xmedia.social.youtube.dto.YouTubeCommentThreadListResponse;
import com.xmedia.social.youtube.dto.YouTubeSearchListResponse;
import com.xmedia.social.youtube.dto.YouTubeTokenResponse;
import com.xmedia.social.youtube.service.YouTubeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/youtube/oauth2")
@RequiredArgsConstructor
public class YouTubeController {

	@Value("${youtube.oauth2.client-id}")
	private String clientId;

	@Value("${youtube.oauth2.redirect-uri}")
	private String redirectUri;

	private final YouTubeService youtubeService;

	@GetMapping("/authorize")
	public ResponseEntity<AuthorizationUrlResponse> authorize() {
		String authorizationUrl = "https://accounts.google.com/o/oauth2/auth?" + "client_id=" + clientId
				+ "&redirect_uri=" + redirectUri + "&response_type=code"
				+ "&scope=https://www.googleapis.com/auth/youtube.readonly%20https://www.googleapis.com/auth/youtube.force-ssl"
				+ "&access_type=offline" + "&prompt=consent";
		return new ResponseEntity<>(new AuthorizationUrlResponse(authorizationUrl), HttpStatus.OK);
	}

	@GetMapping("/callback")
	public ResponseEntity<YouTubeTokenResponse> oauth2Callback(@RequestParam("code") String code) {
		YouTubeTokenResponse tokenResponse = youtubeService.exchangeCodeForTokens(code);
		return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
	}

	@GetMapping("/channel-details")
	public ResponseEntity<YouTubeChannelDetailsResponse> getMyChannelDetails(
			@RequestParam("accessToken") String accessToken) {
		YouTubeChannelDetailsResponse response = youtubeService.getAndSaveMyChannelDetails(accessToken);
		if (response != null && !response.getItems().isEmpty()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // or custom message "No channel available"
		}
	}

	@GetMapping("/comment-threads")
	public ResponseEntity<YouTubeCommentThreadListResponse> getCommentThreads(
			@RequestParam("accessToken") String accessToken, @RequestParam("videoId") String videoId,
			@RequestParam(value = "maxResults", defaultValue = "10") int maxResults) {
		YouTubeCommentThreadListResponse commentThreadListResponse = youtubeService
				.getAndSaveCommentThreads(accessToken, videoId, maxResults);
		if (commentThreadListResponse != null) {
			return new ResponseEntity<>(commentThreadListResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<YouTubeSearchListResponse> search(@RequestParam("accessToken") String accessToken,
			@RequestParam("channelId") String channelId,
			@RequestParam(value = "maxResults", defaultValue = "50") int maxResults,
			@RequestParam(value = "order", defaultValue = "date") String order) {
		YouTubeSearchListResponse searchListResponse = youtubeService.getAndSaveSearchResults(accessToken, channelId,
				maxResults, order);
		if (searchListResponse != null) {
			return new ResponseEntity<>(searchListResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all-video-comments")
	public ResponseEntity<List<VideoCommentsResponse>> getAllVideoComments(
			@RequestParam("accessToken") String accessToken) {
		List<VideoCommentsResponse> comments = youtubeService.getAllVideoComments(accessToken);

		if (comments.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

}
