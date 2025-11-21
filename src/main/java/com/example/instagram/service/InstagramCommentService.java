package com.example.instagram.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.instagram.entity.InstaComment;
import com.example.instagram.entity.InstagramResponseDto;
import com.example.instagram.repository.InstaCommentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstagramCommentService {

	private final InstaCommentRepository repo;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	 

	@Value("${token.file:src/main/resources/token.json}")
	private String tokenFile;

	@Value("${facebook.api-version:v19.0}")
	private String apiVersion;

	@Value("${facebook.app-id:}")
	private String appId;

	@Value("${facebook.redirect-uri:http://localhost:8080/api/facebook/oauth/exchange}")
	private String redirectUri;

	private String appSecret = "cf9964b92f7b53c13a3703f9abe85bf1";

	public InstagramCommentService(InstaCommentRepository repo) {
		this.repo = repo;
	}

	@Transactional
	public void saveCommentsFromInstagramResponse(InstagramResponseDto responseDto) {
		if (responseDto == null || responseDto.getData() == null) {
			return;
		}
		responseDto.getData().forEach(dataItem -> {
			String commentId = dataItem.getId();
			String text = dataItem.getText();
			String timestamp = dataItem.getTimestamp();
			String userId = dataItem.getFrom() != null ? dataItem.getFrom().getId() : null;
			String mediaId = "NOT_AVAILABLE"; // Media ID is not directly in the provided response structure

			saveComment(commentId, mediaId, text, userId, timestamp);
		});
	}

	@Transactional
	public void saveComment(String commentId, String mediaId, String text, String userId, String timestamp) {

		if (commentId == null || commentId.isBlank()) {
			return;
		}

		if (repo.existsById(commentId))
			return;

		InstaComment c = new InstaComment();
		c.setId(commentId);
		c.setMediaId(mediaId);
		c.setText(text);
		c.setUserId(userId);
		// c.setUsername(username);
		c.setTimestamp(timestamp);

		repo.save(c);
	}

	public String generateLoginUrl() {
        try {
        	String encoded = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
            String scope = URLEncoder.encode("instagram_basic,instagram_manage_comments,pages_read_engagement,pages_manage_posts,pages_read_user_content,pages_show_list,public_profile", StandardCharsets.UTF_8);
            return String.format("https://www.facebook.com/%s/dialog/oauth?client_id=%s&redirect_uri=%s&scope=%s&response_type=code", apiVersion, appId, encoded, scope);
            } catch (Exception e) {
            return "ERROR_GENERATING_URL:" + e.getMessage();
        }
    }

	public Map<String,Object> exchangeCodeForLongLivedToken(String code) throws Exception {
        // Exchange code for short-lived
        String shortUrl = String.format("https://graph.facebook.com/%s/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
                apiVersion, appId, java.net.URLEncoder.encode(redirectUri, StandardCharsets.UTF_8), appSecret, java.net.URLEncoder.encode(code, StandardCharsets.UTF_8));
        JsonNode shortResp = callUrl(shortUrl);
        if (!shortResp.has("access_token")) {
            throw new RuntimeException("Failed to obtain short-lived token: " + shortResp.toString());
        }
        String shortToken = shortResp.get("access_token").asText();
 
        // Exchange for long-lived
        String exch = String.format("https://graph.facebook.com/%s/oauth/access_token?grant_type=fb_exchange_token&client_id=%s&client_secret=%s&fb_exchange_token=%s",
                apiVersion, appId, appSecret, java.net.URLEncoder.encode(shortToken, StandardCharsets.UTF_8));
        JsonNode longResp = callUrl(exch);
 
        // add issued_at
        ((com.fasterxml.jackson.databind.node.ObjectNode) longResp).put("issued_at", Instant.now().getEpochSecond());
        saveTokenJson(longResp);
        Map<String,Object> out = objectMapper.convertValue(longResp, Map.class);
        return out;
    }

	public synchronized void saveTokenJson(JsonNode tokenJson) {
		try {
			File f = new File(tokenFile);
			f.getParentFile().mkdirs();
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(f, tokenJson);
		} catch (Exception e) {

		}
	}

	private JsonNode callUrl(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		int rc = conn.getResponseCode();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(rc >= 200 && rc < 300) ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line);
		br.close();
		return objectMapper.readTree(sb.toString());
	}
}
