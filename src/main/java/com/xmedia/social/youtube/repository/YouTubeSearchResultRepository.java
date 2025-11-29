package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeSearchResult;

@Repository
public interface YouTubeSearchResultRepository extends JpaRepository<YouTubeSearchResult, String> {
}
