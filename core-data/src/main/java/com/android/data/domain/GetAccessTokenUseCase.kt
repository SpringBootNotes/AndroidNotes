package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface GetAccessTokenUseCase {
    operator fun invoke(): String?
}

class GetAccessTokenUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
): GetAccessTokenUseCase {
    override fun invoke(): String? {
        return encryptedTokenRepository.getAccessToken()
    }
}