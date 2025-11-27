package com.xmedia.social.googleauth.repository;

import com.xmedia.social.googleauth.model.GoogleToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Long> {
}
