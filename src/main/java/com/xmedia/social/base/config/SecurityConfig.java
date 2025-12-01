package com.xmedia.social.base.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/fb/**", "/api/**", "/youtube/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}


//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/", "/fb/**", "/api/**", "/youtube/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .oauth2Login(oauth -> oauth
//                .loginPage("/oauth2/authorization/google")  // 👈 NO SPRING LOGIN PAGE
//            )
//            .logout(logout -> logout
//                .logoutSuccessUrl("/logout-success")
//                .permitAll()
//            );
//
//        return http.build();
//    }
//}
