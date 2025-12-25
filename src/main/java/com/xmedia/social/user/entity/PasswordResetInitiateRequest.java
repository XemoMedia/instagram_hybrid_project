package com.xmedia.social.user.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class PasswordResetInitiateRequest {
    @NotBlank
    @Email
    private String email;
}
