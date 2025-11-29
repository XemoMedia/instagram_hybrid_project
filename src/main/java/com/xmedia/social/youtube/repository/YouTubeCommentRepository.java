package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeComment;

@Repository
public interface YouTubeCommentRepository extends JpaRepository<YouTubeComment, String> {
	 Optional<YouTubeComment> findByAuthorChannelIdValue(String authorChannelIdValue);
}
