package com.android.login.data

import com.android.data.domain.SaveTokensUseCase
import com.android.login.data.models.LoginRequest
import com.android.login.domain.LoginUseCase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(email: String, password: String): LoginUseCase.LoginResult
}

class LoginRepositoryDefault @Inject constructor(
    private val loginApi: LoginApi,
    private val saveTokensUseCase: SaveTokensUseCase
) : LoginRepository {
    override suspend fun login(email: String, password: String): LoginUseCase.LoginResult {
        return try {
            val result = loginApi.login(LoginRequest(email, password))

            saveTokensUseCase(
                userId = result.userId,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )

            return LoginUseCase.LoginResult.Success
        } catch (_: HttpException) {
            // Handle specific API errors (401, 404, etc)
            LoginUseCase.LoginResult.InvalidCredentialsError
        } catch (_: IOException) {
            // Handle network/connection errors
            LoginUseCase.LoginResult.NetworkError
        } catch (_: Exception) {
            LoginUseCase.LoginResult.GenericError
        }
    }
}
