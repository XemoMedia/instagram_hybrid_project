package com.xmedia.social.user.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.xmedia.social.user.model.Role;
import com.xmedia.social.user.model.UserAccountType;
import com.xmedia.social.user.model.UserType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private UserType userType;
    private UserAccountType accountType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<RoleDTO> roles;
    
    public Set<RoleDTO> getRoles() {
		if (roles == null) {
			roles = new HashSet<>();
		}
		return roles;
	}
    private Set<UserPreferenceDTO> preferences;
    
    public Set<UserPreferenceDTO> getPreferences() {
		if (preferences == null) {
			preferences = new HashSet<>();
		}
		return preferences;
	}
}

