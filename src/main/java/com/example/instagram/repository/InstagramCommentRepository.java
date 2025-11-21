package com.example.instagram.repository;

import com.example.instagram.model.InstagramComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramCommentRepository extends JpaRepository<InstagramComment, String> {
}
