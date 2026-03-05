package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SaveTokensUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk(relaxed = true)
    private val useCase = SaveTokensUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository save called`() {
        val userId = "123"
        val accessToken = "access"
        val refreshToken = "refresh"

        useCase(userId, accessToken, refreshToken)

        verify { repository.save(userId, accessToken, refreshToken) }
    }
}
