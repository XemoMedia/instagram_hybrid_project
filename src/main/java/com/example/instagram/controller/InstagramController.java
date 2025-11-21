package com.example.instagram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.instagram.dto.InstagramResponseDto;
import com.example.instagram.service.InstagramService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/instagram")
public class InstagramController {

	private final InstagramService instaService;

	public InstagramController(InstagramService instaService) {
		this.instaService = instaService;
	}

	@GetMapping("/fetch-by-post-id")
	public ResponseEntity<?> fetch() {
		try {
			InstagramResponseDto response = instaService.fetchInstagramMedia();

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

	@GetMapping("/save-raw")
	public JsonNode saveRawJson() throws Exception {
		return instaService.fetchAndSaveRawResponse();
	}
}
