package com.xmedia.social.instagram.repository;

import com.xmedia.social.instagram.entity.InstagramComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstagramCommentRepository extends JpaRepository<InstagramComment, String> {
	List<InstagramComment> findByFromId(String fromId);

	List<InstagramComment> findByFromUsername(String fromUsername);

	List<InstagramComment> findAll();

	List<InstagramComment> findByMedia_Id(String mediaId);
}

