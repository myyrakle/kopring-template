package com.myyrakle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class EncodeConfig {
    @Bean
    open fun passwordEncoder(): BCryptPasswordEncoder
    {
        return BCryptPasswordEncoder()
    }
}