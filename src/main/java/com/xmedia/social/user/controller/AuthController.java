package com.xmedia.social.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.user.dto.MessageResponse;
import com.xmedia.social.user.entity.PasswordResetConfirmRequest;
import com.xmedia.social.user.model.User;
import com.xmedia.social.user.services.PasswordResetService;
import com.xmedia.social.user.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final PasswordResetService passwordResetService;
	private final UserService userService;

	@GetMapping("/")
	public ResponseEntity<?> googleLogin(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Authentication token missing"));
        }

        var attributes = authentication.getPrincipal().getAttributes();
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String loginType = authentication.getAuthorizedClientRegistrationId();

        User user = userService.saveGoogleUser(name, email,loginType);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getUsername(),
                "email", user.getEmail(),
                "userType", user.getUserType(),
                "accountType", user.getAccountType()
        ));
    }
//	public ResponseEntity<?> googleLogin(Model model, OAuth2AuthenticationToken authentication) {
//		try {
//			if (authentication != null) {
//				var attributes = authentication.getPrincipal().getAttributes();
//				String name = (String) attributes.get("name");
//				String email = (String) attributes.get("email");
//				String loginType = (String) authentication.getAuthorizedClientRegistrationId();
//				return ResponseEntity.ok().body(Map.of("name", name, "email", email, "loginType", loginType));
//			}
//			return ResponseEntity.badRequest().body(new MessageResponse("Authentication token missing"));
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
//		}
//	}

	@PostMapping("/password/reset/confirm")
	public ResponseEntity<?> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
		try {
			String message = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
			return ResponseEntity.ok(new MessageResponse(message));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}

}