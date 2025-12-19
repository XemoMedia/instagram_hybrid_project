package com.xmedia.social.instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.base.enums.VideoStatus;
import com.xmedia.social.instagram.dto.InstagramVideoPost;

@Repository
public interface InstagramVideoRepository
        extends JpaRepository<InstagramVideoPost, Long> {

    List<InstagramVideoPost> findByStatusIn(List<VideoStatus> statuses);
    
    Optional<InstagramVideoPost> findTopByOrderByCreatedAtDesc();
}

