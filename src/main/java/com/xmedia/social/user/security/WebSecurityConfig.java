package com.xmedia.social.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        // ✅ REQUIRED FOR H2 CONSOLE (MOST IMPORTANT)
        .headers(headers ->
            headers.frameOptions(frame -> frame.disable())
        )

        // ✅ H2 console needs CSRF ignored
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
        )

        // ✅ Allow H2 console access
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**", "/", "/error").permitAll()
            .anyRequest().permitAll()
        )

        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .logout(logout -> logout.logoutSuccessUrl("/logout-success"));

    return http.build();
    }
}
