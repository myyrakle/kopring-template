package com.myyrakle.provider

import com.myyrakle.dto.JwtSuccessResponseDto
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.xml.bind.DatatypeConverter

@Component
class JwtProvider {
    private var key: Key? = null

    // 시크릿키 기반으로 provider 생성
    constructor(@Value("\${jwt.secret}") secretKey: String?) {
        val secretByteKey = DatatypeConverter.parseBase64Binary(secretKey)
        key = Keys.hmacShaKeyFor(secretByteKey)
    }

    // JWT 액세스토큰 생성
    open fun generateToken(authentication: Authentication): JwtSuccessResponseDto? {
        val authorities: String = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        // 토큰 만료시간. 30분
        val expireTime = Date(System.currentTimeMillis() + 1000 * 60 * 30);
        
        //Access Token 생성
        val accessToken: String = Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

         return JwtSuccessResponseDto(accessToken)
    }

    // 액세스토큰 기반으로 인가 정보 획득
    fun getAuthentication(accessToken: String): Authentication? {
        //토큰 복호화
        val claims: Claims = parseClaims(accessToken)
        if (claims["auth"] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        var auth: String = claims["auth"].toString()

        val authorities: Collection<GrantedAuthority> = Arrays.stream(Pattern.compile(",").split(auth))
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
        val principal: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    // JWT 토큰 검증
    fun validateToken(accessToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
            return true
        } catch (e: SecurityException) {
            System.out.println("Invalid JWT Token: $e")
        } catch (e: MalformedJwtException) {
            System.out.println("Invalid JWT Token: $e")
        } catch (e: ExpiredJwtException) {
            System.out.println("Expired JWT Token: $e")
        } catch (e: UnsupportedJwtException) {
            System.out.println("Unsupported JWT Token: $e")
        } catch (e: IllegalArgumentException) {
            System.out.println("JWT claims string is empty: $e")
        }
        return false
    }

    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody()
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}