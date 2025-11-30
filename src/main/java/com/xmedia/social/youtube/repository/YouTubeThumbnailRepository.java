package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeThumbnail;

@Repository
public interface YouTubeThumbnailRepository extends JpaRepository<YouTubeThumbnail, String> {
}
