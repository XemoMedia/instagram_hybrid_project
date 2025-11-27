package com.xmedia.social.youtube.repository;

import com.xmedia.social.youtube.entity.YouTubeChannelDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeChannelDataRepository extends JpaRepository<YouTubeChannelDataEntity, Long> {
}
