package com.xmedia.social.user.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xmedia.social.user.model.ERole;
import com.xmedia.social.user.model.Role;
import com.xmedia.social.user.model.User;
import com.xmedia.social.user.model.UserType;
import com.xmedia.social.user.repository.RoleRepository;
import com.xmedia.social.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public User registerNewUser(String username, String email, String password, UserType userType) {
		if (userRepository.existsByUsername(username)) {
			throw new RuntimeException("Username is already taken!");
		}
		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email is already in use!");
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setUserType(userType);

		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);
		user.setRoles(roles);

		return userRepository.save(user);
	}
}
