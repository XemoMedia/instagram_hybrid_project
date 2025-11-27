package com.xmedia.social.facebook.controller;

import com.xmedia.social.facebook.service.FacebookCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facebook/comments")
public class FacebookCommentController {

    @Autowired
    private FacebookCommentService service;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchComments(@RequestParam String postId) {
        try {
            service.fetchAndSaveComments(postId);
            return ResponseEntity.ok("Comments fetched and saved successfully for post: " + postId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching and saving comments: " + e.getMessage());
        }
    }
}
