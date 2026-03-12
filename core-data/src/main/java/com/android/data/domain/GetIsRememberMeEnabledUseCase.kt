package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface GetIsRememberMeEnabledUseCase {
    operator fun invoke(): Boolean
}

class GetIsRememberMeEnabledUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
) : GetIsRememberMeEnabledUseCase {
    override fun invoke(): Boolean {
        return encryptedTokenRepository.getIsRememberMeEnabled()
    }
}
