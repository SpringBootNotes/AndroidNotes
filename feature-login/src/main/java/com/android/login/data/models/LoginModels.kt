package com.android.login.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val userId: String,
    val accessToken: String,
    val refreshToken: String
)
