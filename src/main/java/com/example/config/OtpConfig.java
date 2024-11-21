package com.example.config;

import com.example.util.OtpGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtpConfig {

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator();
    }
}
