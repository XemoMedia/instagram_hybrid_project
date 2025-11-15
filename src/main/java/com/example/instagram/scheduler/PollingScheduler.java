package com.example.instagram.scheduler;

import com.example.instagram.service.InstagramPollingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PollingScheduler {

    private final InstagramPollingService pollingService;

    public PollingScheduler(InstagramPollingService pollingService) {
        this.pollingService = pollingService;
    }

    // Run every 10 minutes (configurable)
    @Scheduled(fixedDelayString = "${polling.delay.ms}")
    public void runPolling() {
        try {
            pollingService.fetchAllMediaAndComments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
