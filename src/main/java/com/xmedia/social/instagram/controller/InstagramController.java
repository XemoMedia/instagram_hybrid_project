package com.xmedia.social.instagram.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.xmedia.social.instagram.dto.ApiPostResponse;
import com.xmedia.social.instagram.dto.InstagramMediaListResponseDto;
import com.xmedia.social.instagram.dto.InstagramResponseDto;
import com.xmedia.social.instagram.service.InstagramPostService;
import com.xmedia.social.instagram.service.InstagramService;
import com.xmedia.social.instagram.service.InstagramVideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instagram")
@RequiredArgsConstructor
@Tag(name = "Instagram", description = "Instagram media and posts management endpoints")
public class InstagramController {

	private final InstagramService instaService;

	private final InstagramPostService postService;

	private final InstagramVideoService videoService;

	@Operation(summary = "Fetch all Instagram posts", description = "Fetches all Instagram posts from the database. If no posts exist in the database, "
			+ "it fetches from Instagram API using the configured user ID and saves them.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Posts fetched successfully", content = @Content(schema = @Schema(implementation = InstagramResponseDto.class))),
			@ApiResponse(responseCode = "500", description = "Server error or Instagram API unavailable") })
	@GetMapping("/fetch-all-insta-post")
	public ResponseEntity<?> fetch() {
		try {
			List<InstagramResponseDto> response = instaService.fetchInstagramMedia();

			// If response is null or empty treat as server down
			if (response == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server is down");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server is down");
		}
	}

	@Operation(summary = "Fetch and save raw Instagram response", description = "Fetches raw JSON response from Instagram API and saves it to the database. "
			+ "Returns the raw JSON response for inspection.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Raw response fetched and saved successfully", content = @Content(schema = @Schema(implementation = JsonNode.class))),
			@ApiResponse(responseCode = "500", description = "Failed to fetch from Instagram API") })
	@GetMapping("/save-raw")
	public JsonNode saveRawJson() throws Exception {
		return instaService.fetchAndSaveRawResponse();
	}

	@Operation(summary = "Get Instagram media list by account ID", description = "Fetches a list of Instagram media items (posts) for a specific Instagram Business account. "
			+ "Requires a valid access token. The response includes media ID, caption, and timestamp.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Media list retrieved successfully", content = @Content(schema = @Schema(implementation = InstagramMediaListResponseDto.class))),
			@ApiResponse(responseCode = "500", description = "Failed to fetch media list") })
	@GetMapping("/{igAccountId}")
	public ResponseEntity<InstagramMediaListResponseDto> getMediaList(
			@Parameter(description = "Instagram Business account ID", required = true, example = "17841478600300872") @PathVariable("igAccountId") String igAccountId,
			@Parameter(description = "Facebook access token", required = true) @RequestParam("access_token") String accessToken) {
		try {
			InstagramMediaListResponseDto comments = instaService.fetchInstagramMediaList(igAccountId, accessToken);
			return ResponseEntity.ok(comments);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/post-image")
	public ResponseEntity<ApiPostResponse> postImage(@RequestParam("image_url") String imageUrl, @RequestParam("caption") String caption) {

		ApiPostResponse response = postService.createAndPublishPost(imageUrl, caption);

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/post-video")
	public ResponseEntity<ApiPostResponse> postVideo(@RequestParam("video_url") String videoUrl, @RequestParam("caption") String caption) {
		return ResponseEntity.ok(videoService.createVideoPost(videoUrl, caption));
	}
}
