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
            // 2. Handle specific API errors (401, 404, etc)
            LoginUseCase.LoginResult.Error("Invalid email or password")
        } catch (_: IOException) {
            // 3. Handle network/connection errors
            LoginUseCase.LoginResult.Error("No internet connection")
        } catch (_: Exception) {
            LoginUseCase.LoginResult.Error("An unexpected error occurred")
        }
    }
}
