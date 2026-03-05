package com.android.login.data

import com.android.data.domain.SaveTokensUseCase
import com.android.login.data.models.LoginRequest
import com.android.login.data.models.LoginResponse
import com.android.login.domain.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.lang.RuntimeException

class LoginRepositoryDefaultTest {
    private val loginApi = mockk<LoginApi>()
    private val saveTokensUseCase = mockk<SaveTokensUseCase>(relaxUnitFun = true)
    private val repository = LoginRepositoryDefault(
        loginApi = loginApi,
        saveTokensUseCase = saveTokensUseCase
    )

    private val email = "e@mail.com"
    private val password = "Password1!"
    private val loginRequest = LoginRequest(
        email = email,
        password = password
    )

    private val successLoginResponse = LoginResponse(
        userId = "userId",
        accessToken = "accessToken",
        refreshToken = "refreshToken"
    )

    @Test
    fun `GIVEN loginApi returns Success WHEN login called THEN call saveTokensUseCase and return LoginResult Success`() =
        runTest {
            coEvery { loginApi.login(loginRequest) } returns successLoginResponse
            val result = repository.login(email, password)

            verify(exactly = 1) {
                saveTokensUseCase(
                    successLoginResponse.userId,
                    successLoginResponse.accessToken,
                    successLoginResponse.refreshToken
                )
            }
            assertTrue(result is LoginUseCase.LoginResult.Success)
        }

    @Test
    fun `GIVEN loginApi returns HttpException WHEN login called THEN return InvalidCredentialsError`() =
        runTest {
            coEvery { loginApi.login(loginRequest) } throws HttpException(mockk(relaxed = true))
            val result = repository.login(email, password)
            assertTrue(result is LoginUseCase.LoginResult.InvalidCredentialsError)
        }

    @Test
    fun `GIVEN loginApi returns IOException WHEN login called THEN return NetworkError`() =
        runTest {
            coEvery { loginApi.login(loginRequest) } throws IOException()
            val result = repository.login(email, password)
            assertTrue(result is LoginUseCase.LoginResult.NetworkError)
        }

    @Test
    fun `GIVEN loginApi returns other error WHEN login called THEN return GenericError`() =
        runTest {
            coEvery { loginApi.login(loginRequest) } throws RuntimeException("Some other error")
            val result = repository.login(email, password)
            assertTrue(result is LoginUseCase.LoginResult.GenericError)
        }

}