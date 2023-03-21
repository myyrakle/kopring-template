package com.myyrakle.modules.auth

import com.myyrakle.modules.auth.dto.LoginRequestDto
import com.myyrakle.modules.auth.dto.LoginResponseDto
import com.myyrakle.provider.JwtProvider
import com.myyrakle.type.CustomUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthController {
    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder

    @PostMapping("login")
    fun login(@RequestBody requestDto: LoginRequestDto): LoginResponseDto {
        // ...
        val authenticationToken = UsernamePasswordAuthenticationToken(requestDto.email, requestDto.password);
 
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        val customUserDetails = authentication.principal as CustomUserDetails

        val accessToken = jwtProvider.generateToken(customUserDetails);

        val response = LoginResponseDto(accessToken ?: "")

        return response
    }
}