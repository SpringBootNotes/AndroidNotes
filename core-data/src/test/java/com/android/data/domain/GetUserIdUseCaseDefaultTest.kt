package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserIdUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk()
    private val useCase = GetUserIdUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository getUserId result returned`() {
        val userId = "123"
        every { repository.getUserId() } returns userId

        val result = useCase()

        assertEquals(userId, result)
    }
}
