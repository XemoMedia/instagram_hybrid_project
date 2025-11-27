package com.xmedia.social.youtube.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.youtube.dto.YouTubeChannelListResponse;
import com.xmedia.social.youtube.dto.YouTubeCommentThreadListResponse;
import com.xmedia.social.youtube.dto.YouTubeSearchListResponse;
import com.xmedia.social.youtube.entity.YouTubeToken;
import com.xmedia.social.youtube.service.YouTubeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YouTubeController {

    private final YouTubeService youTubeService;

    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> youtubeOAuth2Callback(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope) {
        YouTubeToken youTubeToken = youTubeService.handleOAuth2Callback(code, scope);
        return ResponseEntity.ok("YouTube OAuth2 callback successful. Token saved with ID: " + youTubeToken.getId());
    }

    @GetMapping("/channel/my")
    public ResponseEntity<YouTubeChannelListResponse> getMyChannelDetails(
            @RequestParam("youtubeTokenId") Long youtubeTokenId) {
        YouTubeChannelListResponse channelDetails = youTubeService.getMyChannelDetails(youtubeTokenId);
        return ResponseEntity.ok(channelDetails);
    }

    @GetMapping("/search")
    public ResponseEntity<YouTubeSearchListResponse> searchYouTube(
            @RequestParam("youtubeTokenId") Long youtubeTokenId,
            @RequestParam("channelId") String channelId,
            @RequestParam(value = "maxResults", defaultValue = "50") int maxResults,
            @RequestParam(value = "order", defaultValue = "date") String order) {
        YouTubeSearchListResponse searchResults = youTubeService.searchYouTube(youtubeTokenId, channelId, maxResults, order);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/commentThreads")
    public ResponseEntity<YouTubeCommentThreadListResponse> getCommentThreads(
            @RequestParam("youtubeTokenId") Long youtubeTokenId,
            @RequestParam("videoId") String videoId,
            @RequestParam(value = "maxResults", defaultValue = "100") int maxResults) {
        YouTubeCommentThreadListResponse commentThreads = youTubeService.getCommentThreads(youtubeTokenId, videoId, maxResults);
        return ResponseEntity.ok(commentThreads);
    }
}
