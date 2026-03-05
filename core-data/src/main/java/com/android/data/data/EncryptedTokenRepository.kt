package com.android.data.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

interface EncryptedTokenRepository {
    fun save(userId: String, accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getUserId(): String?
    fun clear()
}

class EncryptedTokenRepositoryDefault @Inject constructor(
    @ApplicationContext private val context: Context
): EncryptedTokenRepository {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    override fun save(
        userId: String,
        accessToken: String,
        refreshToken: String
    ) {
        sharedPrefs.edit().apply {
            putString(USER_ID_KEY, userId)
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            apply()
        }
    }

    override fun getAccessToken(): String? {
        return sharedPrefs.getString(ACCESS_TOKEN_KEY, null)
    }

    override fun getRefreshToken(): String? {
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, null)
    }

    override fun getUserId(): String? {
        return sharedPrefs.getString(ACCESS_TOKEN_KEY, null)
    }

    override fun clear() {
        sharedPrefs.edit { clear() }
    }

    private companion object {
        const val FILE_NAME = "secure_tokens"
        const val USER_ID_KEY = "user_id"
        const val ACCESS_TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
    }

}

