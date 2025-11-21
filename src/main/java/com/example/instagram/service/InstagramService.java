package com.example.instagram.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.instagram.config.InstagramFeignClient;
import com.example.instagram.dto.InstagramResponseDto;
import com.example.instagram.model.InstagramComment;
import com.example.instagram.model.InstagramMedia;
import com.example.instagram.model.InstagramReply;
import com.example.instagram.repository.InstagramMediaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InstagramService {

	private final InstagramFeignClient feignClient;
	private final ObjectMapper mapper;
	private final InstagramMediaRepository instagramMediaRepository;

	@Value("${access-token}")
	private String accessToken;
	
	@Value("${ig-user-id}")
	private String igUserId;

	
	private static final DateTimeFormatter IG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	// NO ENCODING!
	private static final String FIELDS = "id,media_type,media_url,thumbnail_url,caption,permalink,timestamp,username,like_count,"
			+ "comments{id,text,timestamp,from{id,username},replies{id,text,timestamp,from{id,username}}}";

	public InstagramService(InstagramFeignClient feignClient, ObjectMapper mapper,
			InstagramMediaRepository instagramMediaRepository) {
		this.feignClient = feignClient;
		this.mapper = mapper;
		this.instagramMediaRepository = instagramMediaRepository;
	}

	public InstagramResponseDto fetchInstagramMedia() throws Exception {

		String json = feignClient.fetchMedia(igUserId, accessToken, FIELDS);

		InstagramResponseDto dto = mapper.readValue(json, InstagramResponseDto.class);

		saveInstagramMedia(dto);

		return dto;
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

}
