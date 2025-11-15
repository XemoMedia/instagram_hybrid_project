package com.example.instagram.controller;

import com.example.instagram.service.InstagramCommentService;
import com.example.instagram.service.InstagramPollingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import com.example.instagram.entity.InstagramResponseDto;

@RestController
@RequestMapping("/webhook/instagram")
public class InstagramWebhookController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final InstagramCommentService commentService;
    private final String VERIFY_TOKEN;
    
    private final InstagramPollingService instagramPollingService;

    public InstagramWebhookController(InstagramCommentService commentService,InstagramPollingService instagramPollingService,
                                      @Value("${app.instagram.verify-token}") String verifyToken) {
        this.commentService = commentService;
        this.instagramPollingService = instagramPollingService;
        this.VERIFY_TOKEN = verifyToken;
    }

    @GetMapping
    public ResponseEntity<String> verify(@RequestParam(name = "hub.mode", required = false) String mode,
                                         @RequestParam(name = "hub.verify_token", required = false) String verifyToken,
                                         @RequestParam(name = "hub.challenge", required = false) String challenge) {
        if ("subscribe".equals(mode) && VERIFY_TOKEN != null && VERIFY_TOKEN.equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Invalid verify token");
    }
    
    @GetMapping("/liveComments")
    public InstagramResponseDto getLiveComments() {

        try {
            InstagramResponseDto response = instagramPollingService.fetchInstagramComments();
            if (response != null) {
                commentService.saveCommentsFromInstagramResponse(response);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: Handle this exception more gracefully, perhaps return an error DTO or throw a custom exception.
            return null; // Or appropriate error handling
        }

    }

    @PostMapping
    public ResponseEntity<String> receive(HttpServletRequest request) {
        try {
            byte[] raw = StreamUtils.copyToByteArray(request.getInputStream());
            String payload = new String(raw, StandardCharsets.UTF_8);
            System.out.println("📥 Incoming Webhook Payload:\n" + payload);

            JsonNode root = mapper.readTree(payload);
            if (!root.has("entry")) {
                System.out.println("⚠ no entry");
                return ResponseEntity.ok("NO_ENTRY");
            }

            for (JsonNode entry : root.get("entry")) {
                JsonNode changes = entry.path("changes");
                if (!changes.isArray()) continue;
                for (JsonNode change : changes) {
                    String field = change.path("field").asText(null);
                    JsonNode value = change.path("value");
                    if ("comments".equals(field)) {
                        // comments on media
                        String commentId = value.path("id").asText(value.path("comment_id").asText(null));
                        String mediaId = value.path("media_id").asText(value.path("media").path("id").asText(null));
                        String text = value.path("text").asText(null);
                        String userId = value.path("from").path("id").asText(null);
                       // String username = value.path("from").path("username").asText(null);
                        String timestamp = value.path("timestamp").asText(Instant.now().toString());

                        commentService.saveComment(commentId, mediaId, text, userId, timestamp);
                    } else if ("live_comments".equals(field)) {
                        String commentId = value.path("id").asText(null);
                        String mediaId = value.path("live_media_id").asText(null);
                        String text = value.path("text").asText(null);
                        String userId = value.path("from").path("id").asText(null);
                        //String username = value.path("from").path("username").asText(null);
                        String timestamp = value.path("created_time").asText(Instant.now().toString());

                        commentService.saveComment(commentId, mediaId, text, userId, timestamp);
                    } else {
                        System.out.println("Skipping field: " + field);
                    }
                }
            }

            return ResponseEntity.ok("EVENT_RECEIVED");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
