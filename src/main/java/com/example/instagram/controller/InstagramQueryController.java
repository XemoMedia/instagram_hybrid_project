package com.example.instagram.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.instagram.model.InstagramMedia;
import com.example.instagram.repository.InstagramMediaRepository;

@RestController
@RequestMapping("/api/instagram/query")
public class InstagramQueryController {

	private final InstagramMediaRepository instagramMediaRepository;

	public InstagramQueryController(InstagramMediaRepository instagramMediaRepository) {
		this.instagramMediaRepository = instagramMediaRepository;
	}

	@GetMapping("/by-username/{username}")
    public List<InstagramMedia> getByUsername(@PathVariable("username") String username) {
        return instagramMediaRepository.findByUsername(username);
    }

	@GetMapping("/by-media-type/{mediaType}")
	public List<InstagramMedia> getByMediaType(@PathVariable("mediaType") String mediaType) {
		return instagramMediaRepository.findByMediaType(mediaType);
	}

	@GetMapping("/between-dates")
	public List<InstagramMedia> getByTimestampRange(@RequestParam("start") String start, @RequestParam("end") String end) {
		LocalDateTime s = LocalDateTime.parse(start);
		LocalDateTime e = LocalDateTime.parse(end);
		return instagramMediaRepository.findByTimestampBetween(s, e);
	}

	@GetMapping("/top10-liked")
	public List<InstagramMedia> getTop10MostLiked() {
		return instagramMediaRepository.findTop10ByOrderByLikeCountDesc();
	}

	@GetMapping("/top10/{username}")
	public List<InstagramMedia> getTop10LatestByUser(@PathVariable("username") String username) {
		return instagramMediaRepository.findTop10ByUsernameOrderByTimestampDesc(username);
	}

	@GetMapping("/search-caption")
	public List<InstagramMedia> searchByCaption(@RequestParam("keyword") String keyword) {
		return instagramMediaRepository.searchByCaption(keyword);
	}
}
