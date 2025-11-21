package com.example.instagram.scheduler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.instagram.service.InstagramService;

@Component
public class PollingScheduler {

    @Autowired
    private InstagramService instagramService;

    // Schedule to run every 60 minutes (3600000 milliseconds)
    //@Scheduled(fixedRate = 3600000)
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
