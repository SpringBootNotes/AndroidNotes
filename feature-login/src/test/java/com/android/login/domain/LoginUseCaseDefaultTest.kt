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

    @Test
    fun `WHEN loginUseCase called THEN loginRepository login result returned`() =
        runTest {
            coEvery { loginRepository.login(email, password) } returns loginResultSuccess
            val result = loginUseCase(email, password)
            assertEquals(loginResultSuccess, result)
        }
}