package com.example.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.instagram.entity.InstagramRawResponse;

@Repository
public interface InstagramRawResponseRepository extends JpaRepository<InstagramRawResponse, Long> {
}

