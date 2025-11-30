package com.xmedia.social.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/fb/**", "/api/**","/youtube/**").permitAll() // Public endpoints
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())  // 👈 required for Spring Boot 3.2+
            .logout(logout -> logout
                .logoutSuccessUrl("/logout-success")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // disable for APIs

        return http.build();
    }
}
