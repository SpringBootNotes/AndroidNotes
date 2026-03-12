package com.android.data.data

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

interface EncryptedTokenRepository {
    fun save(userId: String, accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getUserId(): String?
    fun setIsRememberMeEnabled(isEnabled: Boolean)
    fun getIsRememberMeEnabled(): Boolean
    fun clear()
}

class EncryptedTokenRepositoryDefault @Inject constructor(
    private val sharedPrefs: SharedPreferences
): EncryptedTokenRepository {

    override fun save(
        userId: String,
        accessToken: String,
        refreshToken: String
    ) {
        sharedPrefs.edit {
            putString(USER_ID_KEY, userId)
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    override fun getAccessToken(): String? {
        return sharedPrefs.getString(ACCESS_TOKEN_KEY, null)
    }

    override fun getRefreshToken(): String? {
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, null)
    }

    override fun getUserId(): String? {
        return sharedPrefs.getString(USER_ID_KEY, null)
    }

    override fun setIsRememberMeEnabled(isEnabled: Boolean) {
        sharedPrefs.edit {
            putBoolean(REMEMBER_ME_ENABLED_KEY, isEnabled)
        }
    }

    override fun getIsRememberMeEnabled(): Boolean {
        return sharedPrefs.getBoolean(REMEMBER_ME_ENABLED_KEY, false)
    }

    override fun clear() {
        sharedPrefs.edit { clear() }
    }

    private companion object {
        const val USER_ID_KEY = "user_id"
        const val ACCESS_TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val REMEMBER_ME_ENABLED_KEY = "remember_me_enabled"
    }

}
