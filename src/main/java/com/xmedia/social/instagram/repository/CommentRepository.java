package com.xmedia.social.instagram.repository;

import com.xmedia.social.instagram.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
	List<Comment> findByAccountId(String accountId);

	List<Comment> findByUsername(String username);

	List<Comment> findAll();

	List<Comment> findByMedia_Id(String mediaId);
}

