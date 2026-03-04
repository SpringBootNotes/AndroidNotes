package com.android.login.data

import com.android.login.data.LoginRepositoryDefault.Companion.INVALID_CREDENTIALS_ERROR_MESSAGE
import com.android.login.data.LoginRepositoryDefault.Companion.NO_INTERNET_CONNECTION_ERROR_MESSAGE
import com.android.login.data.LoginRepositoryDefault.Companion.UNKNOWN_ERROR_MESSAGE
import com.android.login.data.models.LoginRequest
import com.android.login.data.models.LoginResponse
import com.android.login.domain.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.lang.RuntimeException

class LoginRepositoryDefaultTest {
    private val loginApi = mockk<LoginApi>()
    private val repository = LoginRepositoryDefault(loginApi)

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
    fun `GIVEN loginApi returns Success WHEN login called THEN return LoginResult Success`() = runTest {
        coEvery { loginApi.login(loginRequest) } returns successLoginResponse
        val result = repository.login(email, password)
        assertTrue(result is LoginUseCase.LoginResult.Success)
    }

    @Test
    fun `GIVEN loginApi returns HttpException WHEN login called THEN return LoginResult Error with invalid credentials message`() = runTest {
        coEvery { loginApi.login(loginRequest) } throws HttpException(mockk(relaxed = true))
        val result = repository.login(email, password)
        assertEquals( LoginUseCase.LoginResult.Error(message =  INVALID_CREDENTIALS_ERROR_MESSAGE), result)
    }

    @Test
    fun `GIVEN loginApi returns IOException WHEN login called THEN return LoginResult Error with no internet message`() = runTest {
        coEvery { loginApi.login(loginRequest) } throws IOException()
        val result = repository.login(email, password)
        assertEquals( LoginUseCase.LoginResult.Error(message =  NO_INTERNET_CONNECTION_ERROR_MESSAGE), result)
    }

    @Test
    fun `GIVEN loginApi returns other error WHEN login called THEN return LoginResult Error with unknown error message`() = runTest {
        coEvery { loginApi.login(loginRequest) } throws RuntimeException("Some other error")
        val result = repository.login(email, password)
        assertEquals( LoginUseCase.LoginResult.Error(message =  UNKNOWN_ERROR_MESSAGE), result)
    }

}