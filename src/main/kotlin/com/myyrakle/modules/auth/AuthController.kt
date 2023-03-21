package com.myyrakle.modules.auth

import com.myyrakle.modules.auth.dto.LoginRequestDto
import com.myyrakle.provider.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController(value="auth")
class AuthController {
    @Autowired
    private lateinit var jwtProvider: JwtProvider;

    @PostMapping("login")
    fun login(@RequestParam requestDto: LoginRequestDto): String {
        // ...
        var authenticationToken = UsernamePasswordAuthenticationToken(memberId, password);
 
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return ""
    }
}