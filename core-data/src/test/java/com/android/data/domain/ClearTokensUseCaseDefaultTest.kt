package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ClearTokensUseCaseDefaultTest {

    private val repository: EncryptedTokenRepository = mockk(relaxed = true)
    private val useCase = ClearTokensUseCaseDefault(repository)

    @Test
    fun `WHEN useCase called THEN repository clear called`() {
        useCase()
        verify { repository.clear() }
    }
}
