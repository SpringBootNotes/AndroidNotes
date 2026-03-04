package com.android.auth.login.data

import com.android.auth.login.data.models.LoginRequest
import com.android.auth.login.domain.LoginUseCase
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginUseCase.LoginResult
}

class LoginRepositoryDefault @Inject constructor(private val loginApi: LoginApi): LoginRepository {
    override suspend fun login(email: String, password: String): LoginUseCase.LoginResult {
        try {
            val result = loginApi.login(LoginRequest(email, password))
            return LoginUseCase.LoginResult.Success(
                userId = result.userId,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )
        } catch(e: Exception) {
            return LoginUseCase.LoginResult.Error
        }
    }
}
