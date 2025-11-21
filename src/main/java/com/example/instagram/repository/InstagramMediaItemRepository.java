package com.example.instagram.repository;

import com.example.instagram.entity.InstagramMediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramMediaItemRepository extends JpaRepository<InstagramMediaItem, String> {
}
