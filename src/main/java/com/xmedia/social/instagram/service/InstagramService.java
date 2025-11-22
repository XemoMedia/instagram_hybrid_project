package com.xmedia.social.instagram.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmedia.social.base.config.InstagramFeignClient;
import com.xmedia.social.instagram.dto.InstagramMediaListResponseDto;
import com.xmedia.social.instagram.dto.InstagramResponseDto;
import com.xmedia.social.instagram.entity.InstagramMediaItem;
import com.xmedia.social.instagram.entity.InstagramRawResponse;
import com.xmedia.social.instagram.entity.InstagramComment;
import com.xmedia.social.instagram.entity.InstagramMedia;
import com.xmedia.social.instagram.entity.InstagramReply;
import com.xmedia.social.instagram.repository.InstagramCommentRepository;
import com.xmedia.social.instagram.repository.InstagramMediaItemRepository;
import com.xmedia.social.instagram.repository.InstagramMediaRepository;
import com.xmedia.social.instagram.repository.InstagramRawResponseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstagramService {

	private static final Logger logger = LoggerFactory.getLogger(InstagramService.class);

	private final InstagramFeignClient feignClient;
	private final ObjectMapper mapper;
	private final InstagramMediaRepository instagramMediaRepository;

	private final InstagramRawResponseRepository instagramRawResponseRepository;
	private final InstagramMediaItemRepository instagramMediaItemRepository;
	private final InstagramCommentRepository instagramCommentRepository;
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

	public InstagramService(InstagramFeignClient feignClient, ObjectMapper mapper,
			InstagramMediaRepository instagramMediaRepository,
			InstagramRawResponseRepository instagramRawResponseRepository, InstagramMediaItemRepository instagramMediaItemRepository,
			InstagramCommentRepository instagramCommentRepository) {
		this.feignClient = feignClient;
		this.mapper = mapper;
		this.instagramMediaRepository = instagramMediaRepository;
		this.instagramRawResponseRepository = instagramRawResponseRepository;
		this.instagramMediaItemRepository = instagramMediaItemRepository;
		this.instagramCommentRepository = instagramCommentRepository;
	}
	
	public List<InstagramResponseDto> fetchInstagramMedia() throws Exception {

	    List<InstagramResponseDto> resultList = new ArrayList<>();

	    List<InstagramMediaItem> mediaItems = instagramMediaItemRepository.findAll();

	    if (mediaItems.isEmpty()) {
	        // If table empty  use igUserId and fetch only once
	    	
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

	            saveInstagramMedia(dto);  // Save every response in DB

	            resultList.add(dto);

	        } catch (Exception ex) {
	            System.out.println("Error fetching media for ID: " + mediaItemId);
	            ex.printStackTrace();
	            // Continue for next ID, don't stop the loop
	        }
	    }

	    return resultList;
	}


//	public InstagramResponseDto fetchInstagramMedia() throws Exception {
//
//		String json;
//		List<InstagramMediaItem> mediaItems = instagramMediaItemRepository.findAll();
//
//		if (mediaItems.isEmpty()) {
//			// Fallback to igUserId from properties if no media items are in the table
//			json = feignClient.fetchMedia(igUserId, accessToken, FIELDS);
//		} else {
//			// Assuming we want to fetch media for the first item found, or you can loop
//			// If you want to fetch for all, you'd need a different return type or to aggregate
//			String mediaItemId = mediaItems.get(0).getId();
//			json = feignClient.fetchMedia(mediaItemId, accessToken, FIELDS);
//		}
//
//		InstagramResponseDto dto = mapper.readValue(json, InstagramResponseDto.class);
//
//		saveInstagramMedia(dto);
//
//		return dto;
//	}

	@Transactional
	public InstagramMediaListResponseDto fetchInstagramMediaList(String igAccountId, String accessToken) throws Exception {
		String fields = "id,caption,timestamp";
		String json = feignClient.fetchMediaList(igAccountId, fields, accessToken);
		InstagramMediaListResponseDto responseDto = mapper.readValue(json, InstagramMediaListResponseDto.class);

		if (responseDto != null && responseDto.getData() != null) {
			List<InstagramMediaItem> mediaItems = responseDto.getData().stream()
					.map(dto -> {
						InstagramMediaItem item = new InstagramMediaItem();
						item.setId(dto.getId());
						item.setIgAccountId(igAccountId);
						item.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), IG_FORMATTER));
						item.setCaption(dto.getCaption());
						return item;
					})
					.collect(Collectors.toList());
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
			InstagramMedia media = instagramMediaRepository.findById(mediaId).orElse(new InstagramMedia());
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
			instagramMediaRepository.save(media);

			JsonNode commentsNode = rootNode.path("comments").path("data");
			if (commentsNode.isArray()) {
				List<InstagramComment> instagramComments = new ArrayList<>();
				for (JsonNode commentNode : commentsNode) {
					InstagramComment comment = new InstagramComment();
					comment.setId(commentNode.path("id").asText());
					comment.setText(commentNode.path("text").asText());
					String commentTimestampStr = commentNode.path("timestamp").asText();
					if (!commentTimestampStr.isEmpty()) {
						comment.setTimestamp(LocalDateTime.parse(commentTimestampStr, IG_FORMATTER));
					}
					comment.setFromId(commentNode.path("from").path("id").asText());
					comment.setFromUsername(commentNode.path("from").path("username").asText());
					comment.setMedia(media);

					JsonNode repliesNode = commentNode.path("replies").path("data");
					if (repliesNode.isArray()) {
						List<InstagramReply> replies = new ArrayList<>();
						for (JsonNode replyNode : repliesNode) {
							InstagramReply reply = new InstagramReply();
							reply.setId(replyNode.path("id").asText());
							reply.setText(replyNode.path("text").asText());
							String replyTimestampStr = replyNode.path("timestamp").asText();
							if (!replyTimestampStr.isEmpty()) {
								reply.setTimestamp(LocalDateTime.parse(replyTimestampStr, IG_FORMATTER));
							}
							reply.setFromId(replyNode.path("from").path("id").asText());
							reply.setFromUsername(replyNode.path("from").path("username").asText());
							reply.setComment(comment);
							replies.add(reply);
						}
						comment.setReplies(replies);
					}
					instagramComments.add(comment);
				}
				instagramCommentRepository.saveAll(instagramComments);
			}
		}
	}

	private void saveInstagramMedia(InstagramResponseDto dto) {

		InstagramMedia media = new InstagramMedia();

		media.setId(dto.getId());
		media.setMediaType(dto.getMediaType());
		media.setMediaUrl(dto.getMediaUrl());
		media.setPermalink(dto.getPermalink());
		media.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), IG_FORMATTER));
		media.setUsername(dto.getUsername());
		media.setLikeCount(dto.getLikeCount());

		if (dto.getComments() != null && dto.getComments().getData() != null) {
			List<InstagramComment> comments = dto.getComments().getData().stream().map(c -> {
				InstagramComment comment = new InstagramComment();

				comment.setId(c.getId());
				comment.setText(c.getText());
				comment.setTimestamp(LocalDateTime.parse(c.getTimestamp(), IG_FORMATTER));

				if (c.getFrom() != null) {
					comment.setFromId(c.getFrom().getId());
					comment.setFromUsername(c.getFrom().getUsername());
				}

				comment.setMedia(media);

				if (c.getReplies() != null && c.getReplies().getData() != null) {
					List<InstagramReply> replies = c.getReplies().getData().stream().map(r -> {
						InstagramReply reply = new InstagramReply();
						reply.setId(r.getId());
						reply.setText(r.getText());
						reply.setTimestamp(LocalDateTime.parse(r.getTimestamp(), IG_FORMATTER));

						if (r.getFrom() != null) {
							reply.setFromId(r.getFrom().getId());
							reply.setFromUsername(r.getFrom().getUsername());
						}

						reply.setComment(comment);
						return reply;
					}).collect(Collectors.toList());

					comment.setReplies(replies);
				} else {
					comment.setReplies(new ArrayList<>());
				}

				return comment;
			}).collect(Collectors.toList());

			media.setComments(comments);
		}

		instagramMediaRepository.save(media);
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

	//@Scheduled(fixedRate = 5000)
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

