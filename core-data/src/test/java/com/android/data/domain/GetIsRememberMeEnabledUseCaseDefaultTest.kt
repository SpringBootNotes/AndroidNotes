package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetIsRememberMeEnabledUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk()
    private val useCase = GetIsRememberMeEnabledUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository getIsRememberMeEnabled result returned`() {
        val isEnabled = true
        every { repository.getIsRememberMeEnabled() } returns isEnabled

        val result = useCase()

        assertEquals(isEnabled, result)
    }
}
