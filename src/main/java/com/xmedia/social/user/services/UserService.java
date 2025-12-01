package com.xmedia.social.user.services;

import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmedia.social.user.model.ERole;
import com.xmedia.social.user.model.Role;
import com.xmedia.social.user.model.User;
import com.xmedia.social.user.model.UserAccountType;
import com.xmedia.social.user.model.UserType;
import com.xmedia.social.user.repository.RoleRepository;
import com.xmedia.social.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

}

