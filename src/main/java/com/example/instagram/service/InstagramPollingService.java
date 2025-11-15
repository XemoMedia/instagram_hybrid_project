package com.example.instagram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.instagram.entity.InstagramResponseDto;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class InstagramPollingService {

    @Value("${access-token}")
    private String accessToken;

    @Value("${ig-user-id}")
    private String igUserId;

    private final InstagramCommentService commentService;
    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public InstagramPollingService(InstagramCommentService commentService) {
        this.commentService = commentService;
        // Spring 3.2 → NO UriBuilderFactory, NO encodingMode
    }

    // -------------------------------------------------------------------------
    // FETCH LATEST COMMENTS (Spring 3.2 Safe)
    // -------------------------------------------------------------------------
    public InstagramResponseDto fetchInstagramComments() throws Exception {

        String fields = URLEncoder.encode("id,text,timestamp,from{id}", "UTF-8");

        String url =
                "https://graph.facebook.com/v19.0/" + igUserId +
                "/comments?access_token=" + accessToken +
                "&fields=" + fields;

        return rest.getForObject(new URI(url), InstagramResponseDto.class);
    }

    // -------------------------------------------------------------------------
    // FETCH ALL MEDIA AND COMMENTS
    // -------------------------------------------------------------------------
    public void fetchAllMediaAndComments() throws Exception {

        String fields = URLEncoder.encode("id,text,timestamp,from{id}", "UTF-8");

        String url =
                "https://graph.facebook.com/v19.0/" + igUserId +
                "/comments?access_token=" + accessToken +
                "&fields=" + fields;

        String mediaResp = rest.getForObject(new URI(url), String.class);
        JsonNode mediaJson = mapper.readTree(mediaResp);

        if (!mediaJson.has("data")) return;

        for (JsonNode media : mediaJson.get("data")) {

            String mediaId = media.path("id").asText(null);

            if (mediaId != null) {
                fetchCommentsForMedia(mediaId);
            }
        }
    }

    // -------------------------------------------------------------------------
    // PAGINATION + COMMENTS FOR EACH MEDIA
    // -------------------------------------------------------------------------
    private void fetchCommentsForMedia(String mediaId) throws Exception {

        String fields = URLEncoder.encode("id,text,timestamp,from{id}", "UTF-8");

        String baseUrl =
                "https://graph.facebook.com/v19.0/" + mediaId +
                "/comments?access_token=" + accessToken +
                "&limit=50&fields=" + fields;

        URI nextUri = new URI(baseUrl);

        while (nextUri != null) {

            String resp = rest.getForObject(nextUri, String.class);
            JsonNode json = mapper.readTree(resp);

            if (json.has("data")) {

                for (JsonNode c : json.get("data")) {

                    String commentId = c.path("id").asText(null);
                    String text = c.path("text").asText(null);
                    String timestamp = c.path("timestamp").asText(null);
                    String userId = c.path("from").path("id").asText(null);

                    commentService.saveComment(commentId, mediaId, text, userId, timestamp);
                }
            }

            // Pagination
            JsonNode paging = json.path("paging");

            if (paging.has("next")) {
                nextUri = new URI(paging.path("next").asText());
            } else {
                nextUri = null;
            }
        }
    }
}
