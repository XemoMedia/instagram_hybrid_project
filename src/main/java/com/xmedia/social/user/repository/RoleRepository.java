package com.xmedia.social.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xmedia.social.user.model.ERole;
import com.xmedia.social.user.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    
}
