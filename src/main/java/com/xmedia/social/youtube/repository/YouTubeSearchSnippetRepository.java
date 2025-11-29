package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeSearchSnippet;

@Repository
public interface YouTubeSearchSnippetRepository extends JpaRepository<YouTubeSearchSnippet, String> {
}
