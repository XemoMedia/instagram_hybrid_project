package com.xmedia.social.youtube.repository;

import com.xmedia.social.youtube.entity.YouTubeSearchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeSearchDataRepository extends JpaRepository<YouTubeSearchData, Long> {
}
