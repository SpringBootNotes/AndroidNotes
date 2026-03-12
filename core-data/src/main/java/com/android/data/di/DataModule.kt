package com.android.data.di


import com.android.data.data.EncryptedTokenRepository
import com.android.data.data.EncryptedTokenRepositoryDefault
import com.android.data.domain.ClearTokensUseCase
import com.android.data.domain.ClearTokensUseCaseDefault
import com.android.data.domain.GetAccessTokenUseCase
import com.android.data.domain.GetAccessTokenUseCaseDefault
import com.android.data.domain.GetIsRememberMeEnabledUseCase
import com.android.data.domain.GetIsRememberMeEnabledUseCaseDefault
import com.android.data.domain.GetRefreshTokenUseCase
import com.android.data.domain.GetRefreshTokenUseCaseDefault
import com.android.data.domain.GetUserIdUseCase
import com.android.data.domain.GetUserIdUseCaseDefault
import com.android.data.domain.SaveTokensUseCase
import com.android.data.domain.SaveTokensUseCaseDefault
import com.android.data.domain.SetIsRememberMeEnabledUseCase
import com.android.data.domain.SetIsRememberMeEnabledUseCaseDefault
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindEncryptedTokenRepository(
        encryptedTokenRepositoryDefault: EncryptedTokenRepositoryDefault
    ): EncryptedTokenRepository

    @Binds
    @Singleton
    abstract fun bindSaveTokensUseCase(
        saveTokensUseCaseDefault: SaveTokensUseCaseDefault
    ): SaveTokensUseCase

    @Binds
    @Singleton
    abstract fun bindClearTokensUseCase(
        clearTokensUseCaseDefault: ClearTokensUseCaseDefault
    ): ClearTokensUseCase

    @Binds
    @Singleton
    abstract fun bindGetAccessTokenUseCase(
        getAccessTokenUseCaseDefault: GetAccessTokenUseCaseDefault
    ): GetAccessTokenUseCase

    @Binds
    @Singleton
    abstract fun bindGetRefreshTokenUseCase(
        getRefreshTokenUseCaseDefault: GetRefreshTokenUseCaseDefault
    ): GetRefreshTokenUseCase

    @Binds
    @Singleton
    abstract fun bindGetUserIdUseCase(
        getUserIdUseCaseDefault: GetUserIdUseCaseDefault
    ): GetUserIdUseCase

    @Binds
    @Singleton
    abstract fun bindSetIsRememberMeEnabledUseCase(
        setIsRememberMeEnabledUseCaseDefault: SetIsRememberMeEnabledUseCaseDefault
    ): SetIsRememberMeEnabledUseCase

    @Binds
    @Singleton
    abstract fun bindGetIsRememberMeEnabledUseCase(
        getIsRememberMeEnabledUseCaseDefault: GetIsRememberMeEnabledUseCaseDefault
    ): GetIsRememberMeEnabledUseCase
}
