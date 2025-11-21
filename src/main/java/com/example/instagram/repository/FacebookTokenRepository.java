package com.example.instagram.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.instagram.entity.FacebookToken;

public interface FacebookTokenRepository extends JpaRepository<FacebookToken, Long> {
	
    // fetch latest token for app_id
    Optional<FacebookToken> findFirstByAppIdOrderByCreatedAtDesc(String appId);
}

