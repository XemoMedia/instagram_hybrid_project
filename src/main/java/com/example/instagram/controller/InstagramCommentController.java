package com.example.instagram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.instagram.dto.InstagramCommentDTO;
import com.example.instagram.service.InstagramCommentService;

@RestController
@RequestMapping("/api/comments")
public class InstagramCommentController {

	@Autowired
	private InstagramCommentService instagramCommentService;

	@GetMapping("/by-userid")
	public ResponseEntity<List<InstagramCommentDTO>> getCommentsByUserId(@RequestParam("fromId") String fromId) {
		List<InstagramCommentDTO> comments = instagramCommentService.getCommentsByFromId(fromId);
		return ResponseEntity.ok(comments);
	}

	@GetMapping("/by-username")
	public ResponseEntity<List<InstagramCommentDTO>> getCommentsByUsername(@RequestParam("username") String username) {
		List<InstagramCommentDTO> comments = instagramCommentService.getCommentsByFromUsername(username);
		return ResponseEntity.ok(comments);
	}

	@GetMapping
	public ResponseEntity<List<InstagramCommentDTO>> getAllComments() {
		List<InstagramCommentDTO> comments = instagramCommentService.findAll();
		return ResponseEntity.ok(comments);
	}

    @GetMapping("/media")
    public ResponseEntity<List<InstagramCommentDTO>> getCommentsByMediaId(@RequestParam("mediaId") String mediaId) {
        List<InstagramCommentDTO> comments = instagramCommentService.getCommentsByMediaId(mediaId);
        return ResponseEntity.ok(comments);
    }
}
