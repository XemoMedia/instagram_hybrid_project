package com.xmedia.social.mail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.mail.entity.EmailConfig;
@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {
	
	Optional<EmailConfig> findByHost(String host);

    boolean existsByHost(String host);

    void deleteByHost(String host);
	}
