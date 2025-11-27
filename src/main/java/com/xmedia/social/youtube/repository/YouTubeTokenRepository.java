package com.xmedia.social.youtube.repository;

import com.xmedia.social.youtube.entity.YouTubeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeTokenRepository extends JpaRepository<YouTubeToken, Long> {
}
