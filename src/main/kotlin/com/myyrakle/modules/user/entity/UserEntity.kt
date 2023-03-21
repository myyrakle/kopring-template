package com.myyrakle.modules.user.entity

import javax.persistence.*

@Entity
@Table(name = "\"user\"")
data class UserEntity(
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val password: String,
)