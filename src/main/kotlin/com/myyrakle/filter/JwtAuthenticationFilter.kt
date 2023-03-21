package com.myyrakle.filter

import com.myyrakle.provider.JwtProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


class JwtAuthenticationFilter(jwtProvider: JwtProvider) : GenericFilterBean() {
    private val jwtProvider: JwtProvider = jwtProvider

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = resolveToken(request as HttpServletRequest)
        
        // 토큰 유효성 검사
        if (token != null && jwtProvider.validateToken(token)) {
            val authentication = jwtProvider.getAuthentication(token)

            if(authentication != null) {
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }

    // 헤더에서 토큰 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")

        if (StringUtils.hasText(bearerToken) && StringUtils.startsWithIgnoreCase(bearerToken, "Bearer ")) {
            return StringUtils.replace(bearerToken, "Bearer ", "");
        } else {
            return null
        }
    }
}
