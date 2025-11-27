package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.youtube.entity.CommentEntity;

public interface YouTubeCommentRepository extends JpaRepository<CommentEntity, String> {
}
