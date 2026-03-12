package com.android.data.domain

import com.android.data.data.EncryptedTokenRepository
import javax.inject.Inject

interface GetUserIdUseCase {
    operator fun invoke(): String?
}

class GetUserIdUseCaseDefault @Inject constructor(
    private val encryptedTokenRepository: EncryptedTokenRepository
): GetUserIdUseCase {
    override fun invoke(): String? {
        return encryptedTokenRepository.getUserId()
    }
}