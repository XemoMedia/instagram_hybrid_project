package com.xmedia.social.googleauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.googleauth.service.GoogleAuthService;
import com.xmedia.social.youtube.dto.GoogleTokenResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/google/token")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

//    public GoogleAuthController(GoogleAuthService googleAuthService) {
//        this.googleAuthService = googleAuthService;
//    }

    @PostMapping("/exchange")
    public ResponseEntity<GoogleTokenResponse> exchangeCode(
            @RequestParam("code") String code) {
        GoogleTokenResponse tokenResponse = googleAuthService.exchangeCodeForTokens(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
