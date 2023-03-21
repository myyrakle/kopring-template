package com.myyrakle.type

import com.myyrakle.modules.user.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import java.util.Collections.emptyList


class CustomUserDetails: UserDetails {
    val id: Long
    val email: String
    val name: String
    private val password: String
    private var grantedAuthorities: MutableList<GrantedAuthority> = emptyList()

    constructor(userEntity: UserEntity, passwordEncoder: PasswordEncoder) {
        id = userEntity.id!!
        email = userEntity.email
        name = userEntity.name
        password = passwordEncoder.encode(userEntity.password)
    }

    open fun getTokenData(): UserToken {
        return UserToken(this.id, this.email, this.name, this.grantedAuthorities)
    }

    open fun addAuthority(authority: GrantedAuthority): CustomUserDetails {
        this.grantedAuthorities.add(authority)
        return this
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.grantedAuthorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.name
    }

    // Implement other UserDetails methods
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}