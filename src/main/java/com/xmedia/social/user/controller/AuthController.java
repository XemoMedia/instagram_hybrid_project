package com.xmedia.social.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.user.dto.MessageResponse;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class AuthController {
	
	@GetMapping("/")
    public ResponseEntity<?> googleLogin(Model model, OAuth2AuthenticationToken authentication) {
		try {
	        if (authentication != null) {
	            var attributes = authentication.getPrincipal().getAttributes();
	            String name = (String) attributes.get("name");
	            String email = (String) attributes.get("email");
	            String loginType = (String) authentication.getAuthorizedClientRegistrationId();
	            return ResponseEntity.ok().body(
	                Map.of("name", name, "email", email,"loginType",loginType)
	            );
	        }
	        return ResponseEntity.badRequest().body(new MessageResponse("Authentication token missing"));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
	    }
	}

   /* private final UserRegistrationService userRegistrationService;
    private final AuthenticationService authenticationService;
    private final SocialLoginService socialLoginService;
    private final PasswordResetService passwordResetService;

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
    }*/
}
