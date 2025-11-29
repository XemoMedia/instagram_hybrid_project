package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeCommentThread;

@Repository
public interface YouTubeCommentThreadRepository extends JpaRepository<YouTubeCommentThread, String> {
	Optional<YouTubeCommentThread> findByTopLevelComment_Id(String commentId);
}
