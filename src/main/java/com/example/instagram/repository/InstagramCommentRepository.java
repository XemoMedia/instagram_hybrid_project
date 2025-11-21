package com.example.instagram.repository;

import com.example.instagram.model.InstagramComment;
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
