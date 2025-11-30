package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.entity.YouTubeChannelDataEntity;

@Repository
public interface YouTubeChannelDataRepository extends JpaRepository<YouTubeChannelDataEntity, Long> {
}




