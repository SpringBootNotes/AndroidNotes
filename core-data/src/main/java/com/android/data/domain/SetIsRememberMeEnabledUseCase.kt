package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface SetIsRememberMeEnabledUseCase {
    operator fun invoke(isEnabled: Boolean)
}

class SetIsRememberMeEnabledUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
) : SetIsRememberMeEnabledUseCase {
    override fun invoke(isEnabled: Boolean) {
        encryptedTokenRepository.setIsRememberMeEnabled(isEnabled)
    }
}
