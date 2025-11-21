package com.example.instagram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.instagram.dto.InstagramResponseDto;
import com.example.instagram.service.InstagramService;

@RestController
@RequestMapping("/api/instagram")
public class InstagramController {

	private final InstagramService instaService;

	public InstagramController(InstagramService instaService) {
		this.instaService = instaService;
	}

	@GetMapping("/fetch")
	public ResponseEntity<InstagramResponseDto> fetch() {
		try {
			InstagramResponseDto response = instaService.fetchInstagramMedia();
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
}
