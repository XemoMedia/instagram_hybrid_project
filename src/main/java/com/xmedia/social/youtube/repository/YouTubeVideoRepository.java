package com.xmedia.social.youtube.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.youtube.entity.YouTubeSearchDataEntity;

public interface YouTubeVideoRepository extends JpaRepository<YouTubeSearchDataEntity, Long> {
	Optional<YouTubeSearchDataEntity> findByVideoId(String videoId);
	
	List<YouTubeSearchDataEntity> findAll();


}
