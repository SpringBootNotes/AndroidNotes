package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface ClearTokensUseCase {
    operator fun invoke()
}

class ClearTokensUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
): ClearTokensUseCase {
    override fun invoke() {
        encryptedTokenRepository.clear()
    }
}
