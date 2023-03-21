package com.myyrakle.provider

import com.myyrakle.modules.user.entity.UserEntity
import com.myyrakle.type.CustomUserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.xml.bind.DatatypeConverter

@Component
class JwtProvider
    (@Value("\${jwt.secret}") secretKey: String?) {
    private var key: Key? = null

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    init {
        val secretByteKey = DatatypeConverter.parseBase64Binary(secretKey)
        key = Keys.hmacShaKeyFor(secretByteKey)
    }

    // JWT 액세스토큰 생성
    fun generateToken(userDetail: CustomUserDetails): String? {
        // 토큰 만료시간. 30분
        val expireTime = Date(System.currentTimeMillis() + 1000 * 60 * 30);

        val tokenData = userDetail.getTokenData();

        val authorities = tokenData.grantedAuthorities.stream().map { e->e.toString() }.collect(Collectors.joining(","))

        //Access Token 생성
        val accessToken: String = Jwts.builder()
            .claim("userId", tokenData.id)
            .claim("email", tokenData.email)
            .claim("name", tokenData.name)
            .claim("authorities", authorities)
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

         return accessToken
    }

    // 액세스토큰 기반으로 인가 정보 획득
    fun getAuthentication(accessToken: String): Authentication? {
        //토큰 복호화
        val claims: Claims = parseClaims(accessToken)
        if (claims["userId"] == null) {
            throw RuntimeException("잘못된 토큰입니다.")
        }

        var userId = claims["userId"].toString().toLong()
        var email: String = claims["email"].toString()
        var name: String = claims["name"].toString()
        var authorities: String = claims["authorities"].toString()

        val grantedAuthorities: Collection<GrantedAuthority> = Arrays.stream( Pattern.compile(",").split(authorities) )
            .filter{ role: String? -> role != "" && role != null }
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())

        val user = UserEntity(userId, name, email, "")

        val principal: UserDetails = CustomUserDetails(user, passwordEncoder)
        return UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities)
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