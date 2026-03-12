package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SetIsRememberMeEnabledUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk(relaxed = true)
    private val useCase = SetIsRememberMeEnabledUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository setIsRememberMeEnabled called`() {
        val isEnabled = true

        useCase(isEnabled)

        verify { repository.setIsRememberMeEnabled(isEnabled) }
    }
}
