package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAccessTokenUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk()
    private val useCase = GetAccessTokenUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository getAccessToken result returned`() {
        val accessToken = "access"
        every { repository.getAccessToken() } returns accessToken

        val result = useCase()

        assertEquals(accessToken, result)
    }
}
