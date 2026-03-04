package com.android.login.domain

import com.android.login.data.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseDefaultTest {
    private val loginRepository: LoginRepository = mockk()
    private val loginUseCase = LoginUseCaseDefault(loginRepository)

    private val email = "e@mail.com"
    private val password = "Password1!"

    private val loginResultSuccess =
        LoginUseCase.LoginResult.Success(
            userId = "userId",
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

    private val loginResultError =
        LoginUseCase.LoginResult.Error(message = "something went wrong")

    @Test
    fun `GIVEN loginRepository returns Success WHEN loginUseCase called THEN Success result returned`() =
        runTest {
            coEvery { loginRepository.login(email, password) } returns loginResultSuccess
            val result = loginUseCase(email, password)
            assertEquals(loginResultSuccess, result)
        }

    @Test
    fun `GIVEN loginRepository returns Error WHEN loginUseCase called THEN Error result returned`() =
        runTest {
            coEvery { loginRepository.login(email, password) } returns loginResultError
            val result = loginUseCase(email, password)
            assertEquals(loginResultError, result)
        }
}