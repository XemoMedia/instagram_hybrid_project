package com.xmedia.social.upload.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xmedia.social.upload.entity.SocialCommentAnalysis;

@Repository
public interface SocialCommentAnalysisRepository extends JpaRepository<SocialCommentAnalysis, String> {

	@Query("select s.msgId from SocialCommentAnalysis s where s.msgId in :msgIds")
	Set<String> findExistingMsgIds(@Param("msgIds") Collection<String> msgIds);

}
