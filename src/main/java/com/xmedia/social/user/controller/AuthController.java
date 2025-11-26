package com.xmedia.social.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.instagram.model.MessageResponse;
import com.xmedia.social.user.auth.service.AuthenticationService;
import com.xmedia.social.user.auth.service.PasswordResetService;
import com.xmedia.social.user.auth.service.SocialLoginService;
import com.xmedia.social.user.auth.service.UserRegistrationService;
import com.xmedia.social.user.entity.LoginRequest;
import com.xmedia.social.user.entity.PasswordResetConfirmRequest;
import com.xmedia.social.user.entity.PasswordResetInitiateRequest;
import com.xmedia.social.user.entity.RegisterRequest;
import com.xmedia.social.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final AuthenticationService authenticationService;
    private final SocialLoginService socialLoginService;
    private final PasswordResetService passwordResetService;



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = userRegistrationService.registerNewUser(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getUserType()
            );
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authenticationService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new MessageResponse(jwt)); // In a real app, return JWT in a proper response DTO
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/password/reset/initiate")
    public ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody PasswordResetInitiateRequest request) {
        try {
            String message = passwordResetService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<?> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        try {
            String message = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Placeholder for social logins
    @PostMapping("/social/google")
    public ResponseEntity<?> googleLogin(@RequestBody String accessToken) {
        String message = socialLoginService.loginWithGoogle(accessToken);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @PostMapping("/social/facebook")
    public ResponseEntity<?> facebookLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        String message = socialLoginService.loginWithFacebook(accessToken);
        return ResponseEntity.ok(new MessageResponse(message));
    }


    @PostMapping("/social/github")
    public ResponseEntity<?> githubLogin(@RequestBody String accessToken) {
        String message = socialLoginService.loginWithGithub(accessToken);
        return ResponseEntity.ok(new MessageResponse(message));
    }
}
