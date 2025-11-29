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
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;
    private final InstagramCommentService instagramCommentService;

  

  

}

