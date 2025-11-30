package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.youtube.model.YouTubeCredential;

@Repository
public interface YouTubeCredentialRepository extends JpaRepository<YouTubeCredential, Long> {
    // Custom query methods can be added here if needed
}
