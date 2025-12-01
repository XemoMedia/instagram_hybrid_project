package com.xmedia.social.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/register", "/api/users/create", "/password/reset/**").permitAll()
				.requestMatchers("/", "/fb/**", "/api/**", "/youtube/**").permitAll().anyRequest().authenticated())
				.oauth2Login(oauth -> oauth.defaultSuccessUrl("/", true))
				.logout(logout -> logout.logoutSuccessUrl("/").permitAll()).csrf(csrf -> csrf.disable());

		return http.build();
	}

	// 🔹 Add this to provide AuthenticationManager bean
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
