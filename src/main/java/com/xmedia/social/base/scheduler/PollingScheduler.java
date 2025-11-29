package com.xmedia.social.base.scheduler;

import org.springframework.stereotype.Component;

import com.xmedia.social.instagram.service.InstagramService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PollingScheduler {

	private InstagramService instagramService;

	// Schedule to run every 60 minutes (3600000 milliseconds)
	// @Scheduled(fixedRate = 3600000)
	public void pollInstagramMediaComments() {
		try {
			instagramService.fetchAndSaveCommentsForAllMediaItems();
			System.out.println("Successfully fetched and saved Instagram media comments.");
		} catch (Exception e) {
			System.err.println("Error fetching and saving Instagram media comments: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
