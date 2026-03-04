package com.android.login.domain

import com.android.login.data.LoginRepository
import javax.inject.Inject

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): LoginResult

    sealed class LoginResult {
        data class Success(val userId: String, val accessToken: String, val refreshToken: String): LoginResult()
        data class Error(val message: String): LoginResult()
    }
}

class LoginUseCaseDefault @Inject constructor(
    private val loginRepository: LoginRepository
): LoginUseCase {
    override suspend fun invoke(email: String, password: String): LoginUseCase.LoginResult {
        return loginRepository.login(email, password)
    }

}