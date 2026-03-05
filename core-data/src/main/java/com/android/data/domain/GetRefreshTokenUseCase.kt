package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface GetRefreshTokenUseCase {
    operator fun invoke(): String?
}

class GetRefreshTokenUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
): GetRefreshTokenUseCase {
    override fun invoke(): String? {
        return encryptedTokenRepository.getRefreshToken()
    }
}