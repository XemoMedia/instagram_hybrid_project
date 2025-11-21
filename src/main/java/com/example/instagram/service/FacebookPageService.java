package com.example.instagram.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class FacebookPageService {

    @Value("${access-token}")
    private String shortLivedToken;

//    @Value("${facebook.long.token:}")
    private String longLivedToken =null;

    @Value("${app_id}")
    private String appId;

    @Value("${app.instagram.app-secret}")
    private String appSecret;

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Step 1 — Exchange short-lived token → long-lived token
     */
    public String generateLongLivedToken() throws Exception {

        String url = "https://graph.facebook.com/v19.0/oauth/access_token"
                + "?grant_type=fb_exchange_token"
                + "&client_id=" + appId
                + "&client_secret=" + appSecret
                + "&fb_exchange_token=" + shortLivedToken;

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
                + "?fields=id,name,access_token,instagram_business_account"
                + "&access_token=" + longLivedToken;

        String resp = rest.getForObject(url, String.class);
        return mapper.readTree(resp);
    }
}


//@Service
//public class FacebookPageService {
//
//    @Value("${access-token}")
//    private String shortLivedToken;
//
//    @Value("${facebook.long.token:}")   // optional on first run
//    private String longLivedToken;
//
//    @Value("${app_id}")
//    private String appId;
//
//    @Value("${app.instagram.app-secret}")
//    private String appSecret;
//
//    private final RestTemplate rest = new RestTemplate();
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    /**
//     * Step 1 — Exchange short-lived token → long-lived token
//     */
//    public String generateLongLivedToken() throws Exception {
//
//        String url = "https://graph.facebook.com/v19.0/oauth/access_token"
//                + "?grant_type=fb_exchange_token"
//                + "&client_id=" + appId
//                + "&client_secret=" + appSecret
//                + "&fb_exchange_token=" + shortLivedToken;
//
//        String resp = rest.getForObject(url, String.class);
//
//        JsonNode json = mapper.readTree(resp);
//
//        longLivedToken = json.get("access_token").asText();
//
//        return longLivedToken;
//    }
//
//    /**
//     * Step 2 — Fetch all pages of the user using LONG-LIVED token
//     */
//    public JsonNode listUserPages() throws Exception {
//        
//        if (longLivedToken == null || longLivedToken.isBlank()) {
//            longLivedToken = generateLongLivedToken();
//        }
//
//        String url = "https://graph.facebook.com/v19.0/me/accounts"
//                + "?access_token=" + longLivedToken;
//
//        String resp = rest.getForObject(url, String.class);
//
//        return mapper.readTree(resp);
//    }
//
//    /**
//     * Get Instagram Business Account ID for a Facebook Page
//     */
//    public JsonNode getPageInstagramBusinessAccount(String pageId, String pageAccessToken) throws Exception {
//
//        String url = "https://graph.facebook.com/v19.0/" + pageId
//                + "?fields=instagram_business_account,name,id"
//                + "&access_token=" + pageAccessToken;
//
//        String resp = rest.getForObject(url, String.class);
//
//        return mapper.readTree(resp);
//    }
//
//    /**
//     * Extract Page Access Token
//     */
//    public String extractPageAccessToken(JsonNode pagesJson, String pageId) {
//        if (!pagesJson.has("data")) return null;
//
//        for (JsonNode page : pagesJson.get("data")) {
//            if (page.get("id").asText().equals(pageId)) {
//                return page.get("access_token").asText(null);
//            }
//        }
//        return null;
//    }
//}
