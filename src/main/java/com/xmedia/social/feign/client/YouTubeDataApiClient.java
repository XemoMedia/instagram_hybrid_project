package com.xmedia.social.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.xmedia.social.youtube.dto.YouTubeChannelListResponse;
import com.xmedia.social.youtube.dto.YouTubeCommentThreadListResponse;
import com.xmedia.social.youtube.dto.YouTubeSearchListResponse;

@FeignClient(name = "youtube-data-api", url = "https://www.googleapis.com/youtube/v3")
public interface YouTubeDataApiClient {

    @GetMapping("/channels")
    YouTubeChannelListResponse getMyChannelDetails(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("part") String part,
            @RequestParam("mine") boolean mine
    );

    @GetMapping("/search")
    YouTubeSearchListResponse search(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("part") String part,
            @RequestParam("channelId") String channelId,
            @RequestParam("maxResults") int maxResults,
            @RequestParam("order") String order
    );

    @GetMapping("/commentThreads")
    YouTubeCommentThreadListResponse getCommentThreads(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("part") String part,
            @RequestParam("videoId") String videoId,
            @RequestParam("maxResults") int maxResults
    );
}
