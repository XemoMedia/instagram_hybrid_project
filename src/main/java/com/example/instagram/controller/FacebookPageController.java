package com.example.instagram.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.instagram.service.FacebookPageService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/fb")
public class FacebookPageController {

	@Autowired
	private FacebookPageService fbService;

	// API - Get dynamic page info (Page ID + IG Business ID)
	@GetMapping("/pages/instagram")
	public JsonNode getPagesWithInstagram() throws Exception {
		return fbService.fetchPagesWithInstagram();
	}
	
	 @GetMapping("/oauth/login")
	    public ResponseEntity<Map<String, String>> loginUrl() {
	        return ResponseEntity.ok(Map.of("login_url", fbService.generateLoginUrl()));
	    }

	    @GetMapping("/oauth/exchange")
	    public ResponseEntity<?> exchange(@RequestParam(required = true) String code) {
	        try {
	            Map<String,Object> token = fbService.exchangeCodeForLongLivedToken(code);
	            return ResponseEntity.ok(token);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
	        }
	    }

	@GetMapping("/instagram-account")
	public ResponseEntity<?> getInstagramBusinessAccountId(@RequestParam("accessToken") String accessToken,
			@RequestParam("fbid") String fbid) {

		try {
			String url = "https://graph.facebook.com/v19.0/" + fbid + "?fields=instagram_business_account&access_token="
					+ accessToken;

			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.getForObject(url, String.class);

			return ResponseEntity.ok(new JSONObject(response).toMap());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
}
