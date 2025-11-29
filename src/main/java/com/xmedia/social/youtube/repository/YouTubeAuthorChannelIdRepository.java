package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeAuthorChannelId;

@Repository
public interface YouTubeAuthorChannelIdRepository extends JpaRepository<YouTubeAuthorChannelId, String> {
	
	Optional<YouTubeAuthorChannelId> findByValue(String value);
}
