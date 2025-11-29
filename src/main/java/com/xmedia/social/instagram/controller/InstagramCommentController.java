package com.xmedia.social.instagram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.instagram.dto.InstagramCommentDto;
import com.xmedia.social.instagram.dto.CommentSentimentDto;
import com.xmedia.social.instagram.service.InstagramCommentService;
import com.xmedia.social.sentimentanalysis.service.SentimentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Instagram Comments", description = "Instagram comments retrieval and management endpoints")
public class InstagramCommentController {

	@Autowired
	private InstagramCommentService instagramCommentService;

	@Autowired
	private SentimentService sentimentService;

	@Operation(
			summary = "Get comments by user ID",
			description = "Retrieves all Instagram comments made by a specific user ID (fromId). " +
					"Returns a list of comments with their text, timestamp, and metadata."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Comments retrieved successfully",
					content = @Content(schema = @Schema(implementation = InstagramCommentDto.class))
			)
	})
	@GetMapping("/by-userid")
	public ResponseEntity<List<InstagramCommentDto>> getCommentsByUserId(
			@Parameter(description = "Instagram user ID who made the comments", required = true, example = "123456789")
			@RequestParam("fromId") String fromId) {
		List<InstagramCommentDto> comments = instagramCommentService.getCommentsByFromId(fromId);
		return ResponseEntity.ok(comments);
	}

	@Operation(
			summary = "Get comments by username",
			description = "Retrieves all Instagram comments made by a specific username. " +
					"Returns a list of comments with their text, timestamp, and metadata."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Comments retrieved successfully",
					content = @Content(schema = @Schema(implementation = InstagramCommentDto.class))
			)
	})
	@GetMapping("/by-username")
	public ResponseEntity<List<InstagramCommentDto>> getCommentsByUsername(
			@Parameter(description = "Instagram username who made the comments", required = true, example = "johndoe")
			@RequestParam("username") String username) {
		List<InstagramCommentDto> comments = instagramCommentService.getCommentsByFromUsername(username);
		return ResponseEntity.ok(comments);
	}


	@Operation(
			summary = "Get all comments",
			description = "Retrieves all Instagram comments stored in the database. " +
					"Use with caution as this may return a large dataset."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "All comments retrieved successfully",
					content = @Content(schema = @Schema(implementation = InstagramCommentDto.class))
			)
	})
	@GetMapping
	public ResponseEntity<List<InstagramCommentDto>> getAllComments() {
		List<InstagramCommentDto> comments = instagramCommentService.findAll();
		return ResponseEntity.ok(comments);
	}

	@Operation(
			summary = "Get comments by media ID",
			description = "Retrieves all comments for a specific Instagram post (media). " +
					"Returns comments associated with the given media ID."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Comments retrieved successfully",
					content = @Content(schema = @Schema(implementation = InstagramCommentDto.class))
			)
	})
    @GetMapping("/media")
    public ResponseEntity<List<InstagramCommentDto>> getCommentsByMediaId(
    		@Parameter(description = "Instagram media/post ID", required = true, example = "17841405309211844")
    		@RequestParam("mediaId") String mediaId) {
        List<InstagramCommentDto> comments = instagramCommentService.getCommentsByMediaId(mediaId);
        return ResponseEntity.ok(comments);
    }
}

