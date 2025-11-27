package com.xmedia.social.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.youtube.entity.AuthorEntity;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByAuthorChannelValue(String authorChannelValue);
}
