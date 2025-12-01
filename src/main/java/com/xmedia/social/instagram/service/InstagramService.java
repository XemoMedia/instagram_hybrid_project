package com.xmedia.social.instagram.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmedia.social.feign.client.InstagramFeignClient;
import com.xmedia.social.instagram.dto.InstagramMediaListResponseDto;
import com.xmedia.social.instagram.dto.InstagramResponseDto;
import com.xmedia.social.instagram.entity.Comment;
import com.xmedia.social.instagram.entity.InstagramMediaItem;
import com.xmedia.social.instagram.entity.InstagramRawResponse;
import com.xmedia.social.instagram.entity.Post;
import com.xmedia.social.instagram.entity.Reply;
import com.xmedia.social.instagram.repository.CommentRepository;
import com.xmedia.social.instagram.repository.InstagramMediaItemRepository;
import com.xmedia.social.instagram.repository.InstagramRawResponseRepository;
import com.xmedia.social.instagram.repository.PostRepository;
import com.xmedia.social.lang.util.LanguageUtil;
import com.xmedia.social.sentimentanalysis.service.SentimentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramService {

	private static final Logger logger = LoggerFactory.getLogger(InstagramService.class);

	private final InstagramFeignClient feignClient;
	private final ObjectMapper mapper;
	private final PostRepository postRepository;

	private final InstagramRawResponseRepository instagramRawResponseRepository;
	private final InstagramMediaItemRepository instagramMediaItemRepository;
	private final CommentRepository commentRepository;
	private final LanguageUtil languageUtil;
	@Value("${ig-account-id}")
	private String igAccountId;

	@Value("${access-token}")
	private String accessToken;

	@Value("${ig-user-id}")
	private String igUserId;

	private static final DateTimeFormatter IG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	// NO ENCODING!
	private static final String FIELDS = "id,media_type,media_url,thumbnail_url,caption,permalink,timestamp,username,like_count,"
			+ "comments{id,text,timestamp,from{id,username},replies{id,text,timestamp,from{id,username}}}";

	private final SentimentService sentimentService;

	public List<InstagramResponseDto> fetchInstagramMedia() throws Exception {

		List<InstagramResponseDto> resultList = new ArrayList<>();

		List<InstagramMediaItem> mediaItems = instagramMediaItemRepository.findAll();

		if (mediaItems.isEmpty()) {
			// If table empty use igUserId and fetch only once

			String json = feignClient.fetchMedia(igUserId, accessToken, FIELDS);

			InstagramResponseDto dto = mapper.readValue(json, InstagramResponseDto.class);
			saveInstagramMedia(dto);
			resultList.add(dto);

			return resultList;
		}

		// Loop through all media items and make API call for each ID
		for (InstagramMediaItem item : mediaItems) {

			String mediaItemId = item.getId();

			try {
				String json = feignClient.fetchMedia(mediaItemId, accessToken, FIELDS);

				InstagramResponseDto dto = mapper.readValue(json, InstagramResponseDto.class);

				saveInstagramMedia(dto); // Save every response in DB

				resultList.add(dto);

			} catch (Exception ex) {
				System.out.println("Error fetching media for ID: " + mediaItemId);
				ex.printStackTrace();
				// Continue for next ID, don't stop the loop
			}
		}

		return resultList;
	}

	@Transactional
	public InstagramMediaListResponseDto fetchInstagramMediaList(String igAccountId, String accessToken)
			throws Exception {
		String fields = "id,caption,timestamp";
		String json = feignClient.fetchMediaList(igAccountId, fields, accessToken);
		InstagramMediaListResponseDto responseDto = mapper.readValue(json, InstagramMediaListResponseDto.class);

		if (responseDto != null && responseDto.getData() != null) {
			List<InstagramMediaItem> mediaItems = responseDto.getData().stream().map(dto -> {
				InstagramMediaItem item = new InstagramMediaItem();
				item.setId(dto.getId());
				item.setIgAccountId(igAccountId);
				item.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), IG_FORMATTER));
				item.setCaption(dto.getCaption());
				return item;
			}).collect(Collectors.toList());
			instagramMediaItemRepository.saveAll(mediaItems);
		}
		return responseDto;
	}

	@Transactional
	public void fetchAndSaveCommentsForAllMediaItems() throws Exception {
		List<InstagramMediaItem> mediaItems = instagramMediaItemRepository.findAll();
		for (InstagramMediaItem mediaItem : mediaItems) {
			String mediaId = mediaItem.getId();
			String json = feignClient.fetchMedia(mediaId, accessToken, FIELDS);
			JsonNode rootNode = mapper.readTree(json);

			// Extract media details (if needed)
			Post media = postRepository.findById(mediaId).orElse(new Post());
			media.setId(rootNode.path("id").asText());
			media.setMediaType(rootNode.path("media_type").asText());
			media.setMediaUrl(rootNode.path("media_url").asText());
			media.setPermalink(rootNode.path("permalink").asText());
			String timestampStr = rootNode.path("timestamp").asText();
			if (!timestampStr.isEmpty()) {
				media.setTimestamp(LocalDateTime.parse(timestampStr, IG_FORMATTER));
			}
			media.setUsername(rootNode.path("username").asText());
			media.setLikeCount(rootNode.path("like_count").asInt());
			postRepository.save(media);

			JsonNode commentsNode = rootNode.path("comments").path("data");
			if (commentsNode.isArray()) {
				List<Comment> instagramComments = new ArrayList<>();
				for (JsonNode commentNode : commentsNode) {
					Comment comment = new Comment();
					comment.setId(commentNode.path("id").asText());
					comment.setText(commentNode.path("text").asText());
					String commentTimestampStr = commentNode.path("timestamp").asText();
					if (!commentTimestampStr.isEmpty()) {
						comment.setTimestamp(LocalDateTime.parse(commentTimestampStr, IG_FORMATTER));
					}
					comment.setAccountId(commentNode.path("from").path("id").asText());
					comment.setUsername(commentNode.path("from").path("username").asText());
					comment.setMedia(media);

					JsonNode repliesNode = commentNode.path("replies").path("data");
					if (repliesNode.isArray()) {
						List<Reply> replies = new ArrayList<>();
						for (JsonNode replyNode : repliesNode) {
							Reply reply = new Reply();
							reply.setId(replyNode.path("id").asText());
							reply.setText(replyNode.path("text").asText());
							String replyTimestampStr = replyNode.path("timestamp").asText();
							if (!replyTimestampStr.isEmpty()) {
								reply.setTimestamp(LocalDateTime.parse(replyTimestampStr, IG_FORMATTER));
							}
							reply.setAccountId(replyNode.path("from").path("id").asText());
							reply.setUsername(replyNode.path("from").path("username").asText());
							reply.setComment(comment);
							replies.add(reply);
						}
						comment.setReplies(replies);
					}
					instagramComments.add(comment);
				}
				commentRepository.saveAll(instagramComments);
			}
		}
	}

	private void saveInstagramMedia(InstagramResponseDto dto) {
		// Build comments with replies first (bottom-up approach)
		List<Comment> comments = new ArrayList<>();
		
		if (dto.getComments() != null && dto.getComments().getData() != null) {
			//String iso =null;
			comments = dto.getComments().getData().stream().map(c -> {
				// Build replies for this comment
				
				String iso = null;
			    if (c.getText()!= null) {
			        iso = languageUtil.detectLanguageIso(c.getText());
			    }
				List<Reply> replies = new ArrayList<>();
				
				
				if (c.getReplies() != null && c.getReplies().getData() != null) {
					iso = languageUtil.detectLanguageIso(c.getText());
					replies = c.getReplies().getData().stream()
						.map(r -> Reply.builder()
							.id(r.getId())
							.text(r.getText())
							.timestamp(LocalDateTime.parse(r.getTimestamp(), IG_FORMATTER))
							.accountId(r.getFrom() != null ? r.getFrom().getId() : null)
							.username(r.getFrom() != null ? r.getFrom().getUsername() : null)
							.build())
						.collect(Collectors.toList());
				}
				
				 c.setLangType(iso);
				
				// Build comment with replies
				return Comment.builder()
					.id(c.getId())
					.text(c.getText())
					.timestamp(LocalDateTime.parse(c.getTimestamp(), IG_FORMATTER))
					.accountId(c.getFrom() != null ? c.getFrom().getId() : null)
					.username(c.getFrom() != null ? c.getFrom().getUsername() : null)
					.replies(replies)
					.languageCode(iso)
					.build();
			}).collect(Collectors.toList());
		}
		
		// Build Post with all comments
		Post media = Post.builder()
			.id(dto.getId())
			.mediaType(dto.getMediaType())
			.mediaUrl(dto.getMediaUrl())
			.permalink(dto.getPermalink())
			.timestamp(LocalDateTime.parse(dto.getTimestamp(), IG_FORMATTER))
			.username(dto.getUsername())
			.likeCount(dto.getLikeCount())
			.comments(comments)
			.build();
		
		// Set bidirectional relationships
		comments.forEach(comment -> {
			comment.setMedia(media);
			comment.getReplies().forEach(reply -> reply.setComment(comment));
		});
		
		// Save once - cascade will save comments and replies
		postRepository.save(media);
		
		// Collect comment IDs and reply IDs for sentiment analysis
		List<String> commentIds = comments.stream()
			.map(Comment::getId)
			.filter(id -> id != null && !id.isEmpty())
			.collect(Collectors.toList());
		
		List<String> repliedIds = comments.stream()
			.flatMap(comment -> comment.getReplies().stream())
			.map(Reply::getId)
			.filter(id -> id != null && !id.isEmpty())
			.collect(Collectors.toList());
		
		// Call Python REST endpoint for sentiment analysis if there are any IDs
		if (!commentIds.isEmpty() || !repliedIds.isEmpty()) {
			try {
				sentimentService.callPythonSentimentAnalysisEndpoint(commentIds, repliedIds);
			} catch (Exception e) {
				logger.error("Error calling Python sentiment analysis endpoint: {}", e.getMessage(), e);
				// Continue execution even if sentiment analysis call fails
			}
		}
	}
	
	public JsonNode fetchAndSaveRawResponse() throws Exception {

		// 1. Get RAW JSON from Instagram
		String json = feignClient.fetchMedia(igUserId, accessToken, FIELDS);

		// 2. Parse JSON to extract ID
		JsonNode node = mapper.readTree(json);
		String mediaId = node.get("id").asText();

		// 3. Store JSON in DB
		InstagramRawResponse raw = new InstagramRawResponse();
		raw.setInstagramId(mediaId);
		raw.setJsonData(json);

		instagramRawResponseRepository.save(raw);

		// Return same JSON to API
		return node;
	}

	// @Scheduled(fixedRate = 5000)
	public void scheduleFetchInstagramMediaList() {
		logger.info("Attempting to fetch Instagram media list...");
		try {
			fetchInstagramMediaList(igAccountId, accessToken);
			logger.info("Instagram media list fetched successfully.");
		} catch (Exception e) {
			logger.error("Error fetching Instagram media list: {}", e.getMessage(), e);
		}
	}
}
