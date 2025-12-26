package com.xmedia.social.user.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmedia.social.user.dto.LoginResponse;
import com.xmedia.social.user.dto.RegisterResponse;
import com.xmedia.social.user.entity.LoginRequest;
import com.xmedia.social.user.entity.RegisterRequest;
import com.xmedia.social.user.model.ERole;
import com.xmedia.social.user.model.Role;
import com.xmedia.social.user.model.User;
import com.xmedia.social.user.model.UserAccountType;
import com.xmedia.social.user.model.UserPreference;
import com.xmedia.social.user.model.UserType;
import com.xmedia.social.user.repository.RoleRepository;
import com.xmedia.social.user.repository.UserPreferenceRepository;
import com.xmedia.social.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
   
	private final UserPreferenceRepository preferenceRepository;
	private final PasswordEncoder passwordEncoder;

//    public User saveGoogleUser(String name, String email, String loginType) {
//
//        return userRepository.findByEmail(email).orElseGet(() -> {
//            // find role by enum
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseGet(() ->
//                            roleRepository.save(Role.builder().name(ERole.ROLE_USER).build())
//                    );
//
//            User newUser = User.builder()
//                    .username(name)
//                    .email(email)
//                    .password("") // Google login → no password
//                    .userType(UserType.GOOGLE)
//                    .accountType(UserAccountType.PERSONAL) // default account type
//                    .build();
//
//            newUser.getRoles().add(userRole);
//
//            return userRepository.save(newUser);
//        });
//    }
    
    @Transactional
    public User saveGoogleUser(String name, String email,String loginType) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseGet(() -> roleRepository.save(Role.builder().name(ERole.ROLE_USER).build()));

                    User newUser = User.builder()
                            .username(name)
                            .email(email)
                            .password("") // no password for Google login
                            .userType(UserType.valueOf(loginType.toUpperCase()))
                            .accountType(UserAccountType.PERSONAL)
                            .roles(new HashSet<>()) 
                            .build();

                    newUser.getRoles().add(userRole);
                    return userRepository.save(newUser);
                });
    }
    
    

	@Transactional

	public User createUser(String username, String email, String password, UserType userType,
			UserAccountType accountType, Set<ERole> roles, Map<String, String> preferences) {

		if (userRepository.findByEmail(email).isPresent()) {
			throw new EmailAlreadyExistsException(email);
		}

		// Fetch or create roles
		Set<Role> roleSet = roles.stream()
				.map(roleName -> roleRepository.findByName(roleName)
						.orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build())))
				.collect(Collectors.toSet());

		// Create user
		User user = User.builder().username(username).email(email).password(password) // hash in real apps
				.userType(userType).accountType(accountType).roles(roleSet).build();

		// Save user first
		userRepository.save(user);

		// Save preferences after user is persisted
		if (preferences != null && !preferences.isEmpty()) {
			Set<UserPreference> prefs = preferences.entrySet().stream().map(
					entry -> UserPreference.builder().key(entry.getKey()).value(entry.getValue()).user(user).build())
					.collect(Collectors.toSet());
			preferenceRepository.saveAll(prefs);
			user.setPreferences(prefs);
		}

		return user;

	}
	
	@Transactional
	public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Login successful
        return new LoginResponse("Login successful", user.getUsername());
    }
	
	@Transactional
    public RegisterResponse registerUser(RegisterRequest request) {

        // Check duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        // Create user object
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Use provided userType OR set default
        user.setUserType(request.getUserType() != null ? request.getUserType() : UserType.NORMAL);

        // Set default account type if needed
        user.setAccountType(UserAccountType.PERSONAL);

        // Set roles
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found!"));

        user.getRoles().add(defaultRole);

        userRepository.save(user);

        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                "User registered successfully!"
        );
    }
	

}

