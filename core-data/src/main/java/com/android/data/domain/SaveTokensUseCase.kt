package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface SaveTokensUseCase {
    operator fun invoke(userId: String, accessToken: String, refreshToken: String)
}

class SaveTokensUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
): SaveTokensUseCase {
    override fun invoke(
        userId: String,
        accessToken: String,
        refreshToken: String
    ) {
        encryptedTokenRepository.save(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

