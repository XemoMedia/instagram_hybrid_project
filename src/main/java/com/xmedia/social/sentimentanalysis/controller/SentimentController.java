package com.xmedia.social.sentimentanalysis.controller;

import com.xmedia.social.sentimentanalysis.dto.*;
import com.xmedia.social.sentimentanalysis.service.SentimentService;
import com.xmedia.social.instagram.dto.InstagramCommentDto;
import com.xmedia.social.instagram.service.InstagramCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for sentiment analysis endpoints.
 */
@RestController
@RequestMapping("/api/v1/sentiment")
@Tag(name = "Sentiment Analysis", description = "Endpoints for analyzing sentiment of comments and posts")
public class SentimentController {

    private final SentimentService sentimentService;
    private final InstagramCommentService instagramCommentService;

    public SentimentController(SentimentService sentimentService, InstagramCommentService instagramCommentService) {
        this.sentimentService = sentimentService;
        this.instagramCommentService = instagramCommentService;
    }

    @PostMapping("/comments")
    @Operation(
        summary = "Analyze sentiment of comments using NLP",
        description = "Analyzes the sentiment of a list of comment texts using Stanford CoreNLP (similar to TextBlob in Python) and returns sentiment labels and polarity scores"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully analyzed comments",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SentimentResponseDto.class),
                examples = @ExampleObject(value = """
                    {
                      "results": [
                        {
                          "text": "This is amazing!",
                          "sentiment": "positive",
                          "polarity": 0.75
                        },
                        {
                          "text": "I hate this product",
                          "sentiment": "negative",
                          "polarity": -0.65
                        }
                      ]
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<SentimentResponseDto> analyzeComments(
            @Valid @RequestBody CommentSentimentRequestDto request) {

        // Use parallel processing for better performance
        List<SentimentService.SentimentAnalysisResult> analyses = 
            sentimentService.analyzeTextsParallel(request.getComments());
        
        List<SentimentResultDto> results = new java.util.ArrayList<>();
        for (int i = 0; i < request.getComments().size() && i < analyses.size(); i++) {
            SentimentService.SentimentAnalysisResult analysis = analyses.get(i);
            results.add(new SentimentResultDto(
                request.getComments().get(i),
                analysis.getLabel(),
                analysis.getPolarity()
            ));
        }

        return ResponseEntity.ok(new SentimentResponseDto(results));
    }

    @PostMapping("/posts")
    @Operation(
        summary = "Analyze sentiment of posts using NLP",
        description = "Analyzes the sentiment of Instagram posts including captions and comments using Stanford CoreNLP (similar to TextBlob in Python). Returns sentiment analysis for each post."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully analyzed posts",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PostsSentimentResponseDto.class),
                examples = @ExampleObject(value = """
                    {
                      "results": [
                        {
                          "postId": "post_123",
                          "caption": {
                            "text": "Beautiful sunset today!",
                            "sentiment": "positive",
                            "polarity": 0.6
                          },
                          "comments": [
                            {
                              "text": "Amazing photo!",
                              "sentiment": "positive",
                              "polarity": 0.7
                            },
                            {
                              "text": "Not my favorite",
                              "sentiment": "negative",
                              "polarity": -0.3
                            }
                          ]
                        }
                      ]
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<PostsSentimentResponseDto> analyzePosts(
            @Valid @RequestBody PostSentimentRequestDto request) {

        // Process posts in parallel for better performance
        List<PostSentimentResultDto> postResults = request.getPosts().parallelStream()
            .map(post -> {
                // Analyze caption
                SentimentService.SentimentAnalysisResult captionAnalysis = 
                    sentimentService.analyzeText(post.getCaption());
                SentimentResultDto captionResult = new SentimentResultDto(
                    post.getCaption(),
                    captionAnalysis.getLabel(),
                    captionAnalysis.getPolarity()
                );

                // Analyze comments in parallel (if any)
                List<SentimentResultDto> commentResults = null;
                if (post.getComments() != null && !post.getComments().isEmpty()) {
                    // Use parallel processing for comments
                    List<SentimentService.SentimentAnalysisResult> commentAnalyses = 
                        sentimentService.analyzeTextsParallel(post.getComments());
                    
                    commentResults = new java.util.ArrayList<>();
                    for (int i = 0; i < post.getComments().size() && i < commentAnalyses.size(); i++) {
                        SentimentService.SentimentAnalysisResult analysis = commentAnalyses.get(i);
                        commentResults.add(new SentimentResultDto(
                            post.getComments().get(i),
                            analysis.getLabel(),
                            analysis.getPolarity()
                        ));
                    }
                }

                return new PostSentimentResultDto(
                    post.getPostId(),
                    captionResult,
                    commentResults
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(new PostsSentimentResponseDto(postResults));
    }

    @GetMapping("/comments/by-username")
    @Operation(
        summary = "Get comments with sentiment analysis from database by username",
        description = "Retrieves all Instagram comments from the database made by a specific username and performs " +
                     "sentiment analysis using NLP (Stanford CoreNLP) on each comment text. Returns text, sentiment label, and polarity score."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Comments with sentiment analysis retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SentimentResponseDto.class),
                examples = @ExampleObject(value = """
                    {
                      "results": [
                        {
                          "text": "This is amazing!",
                          "sentiment": "positive",
                          "polarity": 0.75
                        },
                        {
                          "text": "I hate this product",
                          "sentiment": "negative",
                          "polarity": -0.65
                        }
                      ]
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "404", description = "No comments found for the username")
    })
    public ResponseEntity<SentimentResponseDto> getCommentsWithSentimentByUsername(
            @Parameter(description = "Instagram username who made the comments", required = true, example = "johndoe")
            @RequestParam("username") String username) {
        
        // Fetch comments from database
        List<InstagramCommentDto> comments = instagramCommentService.getCommentsByFromUsername(username);
        
        // Extract texts for batch processing
        List<String> texts = comments.stream()
                .map(InstagramCommentDto::getText)
                .collect(Collectors.toList());
        
        // Analyze sentiment in parallel for better performance
        List<SentimentService.SentimentAnalysisResult> analyses = 
                sentimentService.analyzeTextsParallel(texts);
        
        // Combine results with original comment texts
        List<SentimentResultDto> results = new java.util.ArrayList<>();
        for (int i = 0; i < comments.size() && i < analyses.size(); i++) {
            SentimentService.SentimentAnalysisResult analysis = analyses.get(i);
            results.add(new SentimentResultDto(
                    comments.get(i).getText(),
                    analysis.getLabel(),
                    analysis.getPolarity()
            ));
        }
        
        return ResponseEntity.ok(new SentimentResponseDto(results));
    }
}

