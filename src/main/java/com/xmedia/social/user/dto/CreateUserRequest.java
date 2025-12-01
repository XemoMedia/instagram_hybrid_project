package com.xmedia.social.user.dto;

import java.util.Map;
import java.util.Set;

import com.xmedia.social.user.model.ERole;
import com.xmedia.social.user.model.UserAccountType;
import com.xmedia.social.user.model.UserType;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private UserType userType;
    private UserAccountType accountType;
    private Set<ERole> roles;
    private Map<String, String> preferences;
}

