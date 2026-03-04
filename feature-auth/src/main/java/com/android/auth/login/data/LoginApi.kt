package com.android.auth.login.data

import com.android.auth.login.data.models.LoginRequest
import com.android.auth.login.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}