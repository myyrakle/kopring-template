package com.myyrakle.type

import org.springframework.security.core.GrantedAuthority

data class UserToken(
    val id: Long,
    val email: String,
    val name: String,
    var grantedAuthorities: Collection<GrantedAuthority>
)