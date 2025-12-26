package com.xmedia.social.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmedia.social.user.model.UserPreference;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> { }
