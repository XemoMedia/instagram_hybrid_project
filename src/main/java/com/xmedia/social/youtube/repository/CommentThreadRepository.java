package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.youtube.entity.CommentThreadEntity;

public interface CommentThreadRepository extends JpaRepository<CommentThreadEntity, String> {
}
