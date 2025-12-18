package com.xmedia.social.instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.base.enums.PostStatus;
import com.xmedia.social.instagram.dto.InstagramPost;

@Repository
public interface InstagramPostRepository extends JpaRepository<InstagramPost, Long> {

    List<InstagramPost> findByStatusIn(List<PostStatus> statuses);
}

