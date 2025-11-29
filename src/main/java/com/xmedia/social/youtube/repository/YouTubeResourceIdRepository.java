package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeResourceId;

@Repository
public interface YouTubeResourceIdRepository extends JpaRepository<YouTubeResourceId, String> {
	Optional<YouTubeResourceId> findByKind(String kind);

}
