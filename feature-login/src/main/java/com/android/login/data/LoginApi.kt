package com.android.login.data

import com.android.login.data.models.LoginRequest
import com.android.login.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}