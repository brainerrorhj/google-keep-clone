package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Tắt CSRF cho API
            .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login", "/api/auth/verify").permitAll()  // Cho phép đăng ký, đăng nhập, xác thực OTP mà không cần xác thực
                .anyRequest().authenticated()  // Các yêu cầu còn lại cần xác thực
            .and()
            .formLogin().disable()  // Tắt form login mặc định của Spring Security
            .httpBasic().disable();  // Tắt cơ chế xác thực cơ bản
        return http.build();
    }
}
