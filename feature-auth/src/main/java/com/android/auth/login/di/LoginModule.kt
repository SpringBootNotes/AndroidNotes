package com.android.auth.login.di

import com.android.auth.login.data.LoginApi
import com.android.auth.login.data.LoginRepository
import com.android.auth.login.data.LoginRepositoryDefault
import com.android.auth.login.domain.LoginUseCase
import com.android.auth.login.domain.LoginUseCaseDefault
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginModule {
    @Binds
    abstract fun bindLoginUseCase(
        loginUseCaseDefault: LoginUseCaseDefault
    ): LoginUseCase

    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryDefault: LoginRepositoryDefault
    ): LoginRepository
}

@Module
@InstallIn(SingletonComponent::class)
object LoginSingletonModule {
    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

}