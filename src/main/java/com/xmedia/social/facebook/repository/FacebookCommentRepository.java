package com.xmedia.social.facebook.repository;

import com.xmedia.social.facebook.entity.FacebookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacebookCommentRepository extends JpaRepository<FacebookComment, Long> {
}
