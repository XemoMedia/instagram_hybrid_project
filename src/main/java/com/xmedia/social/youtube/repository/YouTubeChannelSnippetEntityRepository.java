package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeChannelSnippetEntity;

@Repository
public interface YouTubeChannelSnippetEntityRepository extends JpaRepository<YouTubeChannelSnippetEntity, Long> {
	
	@Query("SELECT s FROM YouTubeChannelSnippetEntity s WHERE s.defaultThumbnail.url = :url")
    Optional<YouTubeChannelSnippetEntity> findByDefaultThumbnailUrl(@Param("url") String url);
}

