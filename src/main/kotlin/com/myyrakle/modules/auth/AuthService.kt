package com.myyrakle.modules.auth

import com.myyrakle.modules.user.entity.UserEntity
import com.myyrakle.type.CustomUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService: UserDetailsService {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String): UserDetails  {
        // DB 조회를 했다고 가정
        val user = UserEntity(1, "sssang97@naver.com", "myyrakle", "q1w2e3r4");

        return this.createUserDetails(user)
    }

    // user entity raw 데이터를 security 내장 타입인 UserDetails로 변환
    private fun createUserDetails(user: UserEntity): UserDetails  {
        var details = CustomUserDetails(user, this.passwordEncoder!!)
        return details
    }
}