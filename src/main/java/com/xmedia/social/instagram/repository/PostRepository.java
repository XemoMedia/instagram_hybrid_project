package com.xmedia.social.instagram.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xmedia.social.instagram.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

	List<Post> findByUsername(String username);

	List<Post> findByMediaType(String mediaType);

	List<Post> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

	List<Post> findTop10ByOrderByLikeCountDesc();

	List<Post> findTop10ByUsernameOrderByTimestampDesc(String username);

	@Query("SELECT m FROM Post m WHERE LOWER(m.caption) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Post> searchByCaption(@Param("keyword") String keyword);
}

