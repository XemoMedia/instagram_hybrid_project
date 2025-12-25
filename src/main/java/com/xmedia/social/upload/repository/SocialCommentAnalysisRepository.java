package com.xmedia.social.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.upload.entity.SocialCommentAnalysis;

@Repository
public interface SocialCommentAnalysisRepository extends JpaRepository<SocialCommentAnalysis, String> {
}
