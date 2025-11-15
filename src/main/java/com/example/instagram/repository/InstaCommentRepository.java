package com.example.instagram.repository;

import com.example.instagram.entity.InstaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstaCommentRepository extends JpaRepository<InstaComment, String> {
}
