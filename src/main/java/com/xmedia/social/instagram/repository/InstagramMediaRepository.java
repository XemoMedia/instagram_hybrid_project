package com.xmedia.social.instagram.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xmedia.social.instagram.entity.InstagramMedia;

@Repository
public interface InstagramMediaRepository extends JpaRepository<InstagramMedia, String> {

	List<InstagramMedia> findByUsername(String username);

	List<InstagramMedia> findByMediaType(String mediaType);

	List<InstagramMedia> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

	List<InstagramMedia> findTop10ByOrderByLikeCountDesc();

	List<InstagramMedia> findTop10ByUsernameOrderByTimestampDesc(String username);

	@Query("SELECT m FROM InstagramMedia m WHERE LOWER(m.caption) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<InstagramMedia> searchByCaption(@Param("keyword") String keyword);
}

