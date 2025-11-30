package com.xmedia.social.youtube.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.youtube.entity.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByAuthorChannelValue(String authorChannelValue);
}
