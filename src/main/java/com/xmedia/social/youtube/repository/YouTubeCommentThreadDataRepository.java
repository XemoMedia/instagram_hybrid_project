package com.xmedia.social.youtube.repository;

import com.xmedia.social.youtube.entity.YouTubeCommentThreadDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeCommentThreadDataRepository extends JpaRepository<YouTubeCommentThreadDataEntity, Long> {
}
