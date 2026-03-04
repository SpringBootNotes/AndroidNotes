package com.android.login.data

import com.android.login.data.models.LoginRequest
import com.android.login.domain.LoginUseCase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginUseCase.LoginResult
}

class LoginRepositoryDefault @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(email: String, password: String): LoginUseCase.LoginResult {
        return try {
            val result = loginApi.login(LoginRequest(email, password))

            return LoginUseCase.LoginResult.Success(
                userId = result.userId,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )
        } catch (_: HttpException) {
            // Handle specific API errors (401, 404, etc)
            LoginUseCase.LoginResult.Error(INVALID_CREDENTIALS_ERROR_MESSAGE)
        } catch (_: IOException) {
            // Handle network/connection errors
            LoginUseCase.LoginResult.Error(NO_INTERNET_CONNECTION_ERROR_MESSAGE)
        } catch (_: Exception) {
            LoginUseCase.LoginResult.Error(UNKNOWN_ERROR_MESSAGE)
        }
    }

    internal companion object {
        const val INVALID_CREDENTIALS_ERROR_MESSAGE = "Invalid email or password"
        const val NO_INTERNET_CONNECTION_ERROR_MESSAGE = "No internet connection"
        const val UNKNOWN_ERROR_MESSAGE = "An unexpected error occurred"
    }
}
