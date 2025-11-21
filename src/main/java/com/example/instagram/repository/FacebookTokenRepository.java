package com.example.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.instagram.entity.FacebookToken;

public interface FacebookTokenRepository extends JpaRepository<FacebookToken, Long> {
}

