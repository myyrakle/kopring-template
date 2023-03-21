package com.myyrakle.config

import com.myyrakle.filter.JwtAuthenticationFilter
import com.myyrakle.modules.auth.AuthService
import com.myyrakle.provider.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired
    private lateinit var jwtProvider: JwtProvider;
    @Autowired
    private lateinit var customUserDetailsService: AuthService

    constructor(jwtProvider: JwtProvider)
    {
        this.jwtProvider = jwtProvider;
    }

    @Bean // 인증 실패시 401 에러를 리턴
    open fun restAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException ->
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        }
    }

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain
    {
        return http
            .httpBasic().disable() 
            .formLogin().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/", "/hello", "/auth/login").permitAll() // 인증 불필요
            .antMatchers("/user/**").authenticated() // 인증 필수
        .and()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint())
        .and()
            .addFilterBefore(JwtAuthenticationFilter (jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
        .build()
    }

    fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(customUserDetailsService)
    }
}