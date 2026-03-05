package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRefreshTokenUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk()
    private val useCase = GetRefreshTokenUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository getRefreshToken result returned`() {
        val refreshToken = "refresh"
        every { repository.getRefreshToken() } returns refreshToken

        val result = useCase()

        assertEquals(refreshToken, result)
    }
}
