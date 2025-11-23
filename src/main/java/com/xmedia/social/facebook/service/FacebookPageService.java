package com.xmedia.social.facebook.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmedia.social.facebook.dto.FbTokenResponseDto;
import com.xmedia.social.facebook.entity.FacebookToken;
import com.xmedia.social.facebook.repository.FacebookTokenRepository;
import com.xmedia.social.mail.entity.EmailConfig;
import com.xmedia.social.mail.entity.EmailTemplate;
import com.xmedia.social.mail.repository.EmailConfigRepository;
import com.xmedia.social.mail.service.EmailTemplateService;
import com.xmedia.social.utility.EmailUtility;

@Service
public class FacebookPageService {

	@Value("${access-token}")
	private String shortLivedToken;

//    @Value("${facebook.long.token:}")
	private String longLivedToken = null;

	@Autowired
	private FacebookTokenRepository tokenRepo;
	
	@Autowired
	private EmailTemplateService emailTemplateService;
	
	@Autowired
	private EmailConfigRepository emailConfigRepository;
	
	@Autowired
	private EmailUtility emailUtility;

	@Value("${app_id}")
	private String appId;

	@Value("${app.instagram.app-secret}")
	private String appSecret;

	@Value("${facebook.redirect-uri:http://localhost:8080/fb/oauth/exchange}")
	private String redirectUri;

	@Value("${facebook.api-version:v19.0}")
	private String apiVersion;

	@Value("${token.file:src/main/resources/token.json}")
	private String tokenFile;
	
	private final RestTemplate rest = new RestTemplate();
	private final ObjectMapper mapper = new ObjectMapper();
	
	

	/**
	 * Step 1 — Exchange short-lived token → long-lived token
	 */
	public String generateLongLivedToken() throws Exception {

		String url = "https://graph.facebook.com/v19.0/oauth/access_token" + "?grant_type=fb_exchange_token"
				+ "&client_id=" + appId + "&client_secret=" + appSecret + "&fb_exchange_token=" + shortLivedToken;

		String resp = rest.getForObject(url, String.class);
		JsonNode json = mapper.readTree(resp);

		longLivedToken = json.get("access_token").asText();
		return longLivedToken;
	}

	/**
	 * Step 2 — Fetch pages with Instagram Business ID dynamically
	 */
	public JsonNode fetchPagesWithInstagram() throws Exception {

		if (longLivedToken == null || longLivedToken.isBlank()) {
			longLivedToken = generateLongLivedToken();
		}

		String url = "https://graph.facebook.com/v19.0/me/accounts"
				+ "?fields=id,name,access_token,instagram_business_account" + "&access_token=" + longLivedToken;

		String resp = rest.getForObject(url, String.class);
		return mapper.readTree(resp);
	}

	public String generateLoginUrl( String emailTo) {
		try {
			String encoded = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
			String scope = URLEncoder.encode(
					"instagram_basic,instagram_manage_comments,pages_read_engagement,pages_manage_posts,pages_read_user_content,pages_show_list,public_profile",
					StandardCharsets.UTF_8);
			String returnUrl =  String.format(
					"https://www.facebook.com/%s/dialog/oauth?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
					apiVersion, appId, encoded, scope);
			
			//String body = template.getBody().replace("{{userName}}", "Hi").replace("{{link}}", returnUrl);
			EmailTemplate emailTemplate = emailTemplateService.getTemplateByName("TOKEN_GEN_MAIL");
			String body = emailTemplate.getBody().replace("{{userName}}", "Hi").replace("{{link}}", returnUrl);
			emailTemplate.setBody(body);
			Optional<EmailConfig> emailConfig = emailConfigRepository.findByHost("smtp.gmail.com");
			if(emailConfig.isPresent()) {
				emailConfig.get().setTo(emailTo);
				emailUtility.sendEmail(emailTemplate,emailConfig.get());
			}
			
			return returnUrl;
		} catch (Exception e) {
			return "ERROR_GENERATING_URL:" + e.getMessage();
		}
	}

	public Map<String, Object> exchangeCodeForLongLivedToken(String code) throws Exception {

		// ========= STEP 1: Get Short-Lived Token =========
		String shortUrl = String.format(
				"https://graph.facebook.com/%s/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s",
				apiVersion, appId, java.net.URLEncoder.encode(redirectUri, StandardCharsets.UTF_8), appSecret,
				java.net.URLEncoder.encode(code, StandardCharsets.UTF_8));

		JsonNode shortResp = callUrl(shortUrl);
		if (!shortResp.has("access_token")) {
			throw new RuntimeException("Failed to obtain short-lived token: " + shortResp.toString());
		}
		String shortToken = shortResp.get("access_token").asText();

		// ========= STEP 2: Get Long-Lived Token =========
		String exch = String.format(
				"https://graph.facebook.com/%s/oauth/access_token?grant_type=fb_exchange_token&client_id=%s&client_secret=%s&fb_exchange_token=%s",
				apiVersion, appId, appSecret, java.net.URLEncoder.encode(shortToken, StandardCharsets.UTF_8));

		JsonNode longResp = callUrl(exch);

		// ========= STEP 3: Add issued_at =========
		((com.fasterxml.jackson.databind.node.ObjectNode) longResp).put("issued_at", Instant.now().getEpochSecond());

		// ========= STEP 4: GET APP ID USING DEBUG TOKEN =========
		JsonNode debug = debugToken(longResp.get("access_token").asText());
		if (debug.has("data") && debug.get("data").has("app_id")) {
			((com.fasterxml.jackson.databind.node.ObjectNode) longResp).put("app_id",
					debug.get("data").get("app_id").asText());
		}

		// Save JSON to DB
		saveTokenJson(longResp);

		saveTokenJsonDb(longResp);

		return mapper.convertValue(longResp, Map.class);
	}

	public JsonNode debugToken(String accessToken) throws Exception {

		// App Token = app_id|app_secret
		String appToken = appId + "|" + appSecret;

		String url = String.format("https://graph.facebook.com/debug_token?input_token=%s&access_token=%s",
				java.net.URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
				java.net.URLEncoder.encode(appToken, StandardCharsets.UTF_8));

		// Call Facebook Debug API
		return callUrl(url);
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
		return mapper.readTree(sb.toString());
	}

	public synchronized void saveTokenJson(JsonNode tokenJson) {
		try {
			File f = new File(tokenFile);
			f.getParentFile().mkdirs();
			mapper.writerWithDefaultPrettyPrinter().writeValue(f, tokenJson);
		} catch (Exception e) {

		}
	}

	public void saveTokenJsonDb(JsonNode root) {
		FacebookToken token = new FacebookToken();

		token.setAccessToken(root.path("access_token").asText());
		token.setAppId(root.path("app_id").asText());
		token.setTokenType(root.path("token_type").asText());

		token.setIssuedAt(LocalDateTime.now());

		// expires_in LocalDateTime
		long expiresInSeconds = root.path("expires_in").asLong();
		token.setExpiresIn(LocalDateTime.now().plusSeconds(expiresInSeconds));

		tokenRepo.save(token);
	}
	
	public FbTokenResponseDto getTokenInfo(String appId) {
		FacebookToken token = tokenRepo.findFirstByAppIdOrderByCreatedAtDesc(appId)
	            .orElse(null);

	    if (token == null) {
	        return null;
	    }

	    return new FbTokenResponseDto(
	            token.getAccessToken(),
	            token.getExpiresIn()
	    );
	}

}

