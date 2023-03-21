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
        val authenticationToken = UsernamePasswordAuthenticationToken(requestDto.email, requestDto.password);
 
        // loadUserByUsername 및 password 체크 수행
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        val customUserDetails = authentication.principal as CustomUserDetails

        val accessToken = jwtProvider.generateToken(customUserDetails);

        val response = LoginResponseDto(accessToken ?: "")

        return response
    }
}