package com.myyrakle.modules.user

import com.myyrakle.modules.user.entity.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    fun findUserById(id: Long): UserEntity  {
        // DB 조회를 했다고 가정
        val user = UserEntity(1, "sssang97@naver.com", "myyrakle", "q1w2e3r4");

        return user
    }
}