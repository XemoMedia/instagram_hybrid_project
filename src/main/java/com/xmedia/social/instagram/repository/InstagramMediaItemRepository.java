package com.xmedia.social.instagram.repository;

import com.xmedia.social.instagram.entity.InstagramMediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramMediaItemRepository extends JpaRepository<InstagramMediaItem, String> {
}

