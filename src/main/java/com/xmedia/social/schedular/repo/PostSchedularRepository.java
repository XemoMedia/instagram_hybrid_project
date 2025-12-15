package com.xmedia.social.schedular.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.schedular.entity.ScheduledPostEntity;


public interface PostSchedularRepository extends JpaRepository<ScheduledPostEntity, Long> {
	
	
} 
