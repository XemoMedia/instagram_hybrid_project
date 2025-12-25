package com.xmedia.social.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.user.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}

