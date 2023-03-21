package com.myyrakle.modules.user

import com.myyrakle.modules.auth.dto.LoginRequestDto
import com.myyrakle.modules.auth.dto.LoginResponseDto
import com.myyrakle.modules.user.dto.GetMyInfoResponseDto
import com.myyrakle.type.CustomUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController {
    @GetMapping("my-info")
    fun getMyInfo(@AuthenticationPrincipal user: CustomUserDetails): GetMyInfoResponseDto {
        return GetMyInfoResponseDto(user.id, user.email, user.username)
    }
}