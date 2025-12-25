package com.xmedia.social.user.services;

import com.xmedia.social.user.model.User;
import com.xmedia.social.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    public PasswordResetService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    // In a real application, this would involve generating a token, storing it,
    // and sending it to the user's email address.
    public String initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with this email not found.");
        }
        // Logic to generate a password reset token and send it via email
        return "Password reset initiated. Check your email (placeholder).";
    }

    // In a real application, this would involve validating the token received
    // from the user before allowing the password change.
    public String resetPassword(String token, String newPassword) {
        // Logic to validate the token and find the user associated with it
        // For now, let's assume we can find the user directly (not recommended for production)
        // In a real scenario, the token would be used to identify the user securely.
        Optional<User> userOptional = userRepository.findAll().stream()
                                                .filter(user -> "dummy_token".equals(token)) // Placeholder: replace with actual token validation
                                                .findFirst();
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid or expired password reset token.");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password has been reset successfully.";
    }
}
