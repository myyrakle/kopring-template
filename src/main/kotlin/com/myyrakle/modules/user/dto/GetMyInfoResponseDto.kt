package com.myyrakle.modules.user.dto

data class GetMyInfoResponseDto (
    val userId: Long,
    val email: String,
    val name: String,
)