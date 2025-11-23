package com.xmedia.social.facebook.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xmedia.social.facebook.dto.FbTokenResponseDto;
import com.xmedia.social.facebook.service.FacebookPageService;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fb")
@Tag(name = "Facebook", description = "Facebook OAuth and page management endpoints")
public class FacebookPageController {

	@Autowired
	private FacebookPageService fbService;

	@Operation(
			summary = "Get Facebook pages with Instagram Business accounts",
			description = "Retrieves all Facebook pages associated with the authenticated user that have linked Instagram Business accounts. " +
					"Returns page ID, name, access token, and Instagram Business account ID."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Successfully retrieved pages with Instagram accounts",
					content = @Content(
							schema = @Schema(implementation = JsonNode.class),
							examples = @ExampleObject(value = "{\"data\":[{\"id\":\"123\",\"name\":\"My Page\",\"instagram_business_account\":{\"id\":\"456\"}}]}")
					)
			),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/pages/instagram")
	public JsonNode getPagesWithInstagram() throws Exception {
		return fbService.fetchPagesWithInstagram();
	}

	@Operation(
			summary = "Generate Facebook OAuth login URL",
			description = "Generates a Facebook OAuth login URL that users can visit to authorize the application. " +
					"After authorization, users will be redirected back with an authorization code."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Login URL generated successfully",
					content = @Content(
							examples = @ExampleObject(value = "{\"login_url\":\"https://www.facebook.com/v19.0/dialog/oauth?client_id=...\"}")
					)
			)
	})
	@GetMapping("/oauth/login")
	public ResponseEntity<Map<String, String>> loginUrl(@RequestParam String email) {
		return ResponseEntity.ok(Map.of("login_url", fbService.generateLoginUrl(email)));
	}

	@Operation(
			summary = "Exchange authorization code for long-lived token",
			description = "Exchanges a short-lived authorization code for a long-lived access token. " +
					"The code is obtained from the OAuth redirect after user authorization."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Token exchanged successfully",
					content = @Content(
							examples = @ExampleObject(value = "{\"access_token\":\"...\",\"token_type\":\"bearer\",\"expires_in\":5184000}")
					)
			),
			@ApiResponse(responseCode = "500", description = "Failed to exchange token")
	})
	@GetMapping("/oauth/exchange")
	public ResponseEntity<?> exchange(
			@Parameter(description = "Authorization code from OAuth redirect", required = true, example = "AQB...")
			@RequestParam(required = true) String code) {
		try {
			Map<String, Object> token = fbService.exchangeCodeForLongLivedToken(code);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
		}
	}

	@Operation(
			summary = "Get Instagram Business account ID from Facebook page",
			description = "Retrieves the Instagram Business account ID associated with a Facebook page using the page ID."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Instagram Business account ID retrieved successfully",
					content = @Content(
							examples = @ExampleObject(value = "{\"instagram_business_account\":{\"id\":\"17841478600300872\"}}")
					)
			),
			@ApiResponse(responseCode = "400", description = "Bad request - Invalid access token or page ID")
	})
	@GetMapping("/instagram-account")
	public ResponseEntity<?> getInstagramBusinessAccountId(
			@Parameter(description = "Facebook access token", required = true)
			@RequestParam("accessToken") String accessToken,
			@Parameter(description = "Facebook page ID", required = true, example = "123456789")
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
	
	@Operation(
			summary = "Get Facebook token information by App ID",
			description = "Retrieves the latest stored access token and expiration information for a specific Facebook App ID."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Token information retrieved successfully",
					content = @Content(schema = @Schema(implementation = FbTokenResponseDto.class))
			),
			@ApiResponse(responseCode = "404", description = "Token not found for the provided App ID")
	})
	@GetMapping("/token/{appId}")
	public ResponseEntity<?> getToken(
			@Parameter(description = "Facebook App ID", required = true, example = "1989904388463671")
			@PathVariable String appId) {

	    FbTokenResponseDto dto = fbService.getTokenInfo(appId);

	    if (dto == null) {
	        return ResponseEntity.status(404)
	                .body("Token not found for appId: " + appId);
	    }

	    return ResponseEntity.ok(dto);
	}


}

