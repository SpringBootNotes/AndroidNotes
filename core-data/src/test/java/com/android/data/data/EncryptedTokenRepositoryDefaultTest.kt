package com.android.data.data

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class EncryptedTokenRepositoryDefaultTest {

    private val sharedPrefs: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private lateinit var repository: EncryptedTokenRepositoryDefault
    private val storage = mutableMapOf<String, String?>()

    private val userId = "123"
    private val accessToken = "access"
    private val refreshToken = "refresh"

    @Before
    fun setup() {
        storage.clear()
        every { sharedPrefs.edit() } returns editor
        every { editor.putString(any(), any()) } answers {
            storage[firstArg()] = secondArg()
            editor
        }
        every { editor.apply() } returns Unit
        every { editor.clear() } answers {
            storage.clear()
            editor
        }
        every { sharedPrefs.getString(any(), any()) } answers {
            storage[firstArg()] ?: secondArg()
        }
        repository = EncryptedTokenRepositoryDefault(sharedPrefs)
    }

    @Test
    fun `WHEN save called THEN then userId, accessToken, and refreshToken saved to preferences`() {
        assertNotEquals(userId, repository.getUserId())
        assertNotEquals(accessToken, repository.getAccessToken())
        assertNotEquals(refreshToken, repository.getRefreshToken())

        repository.save(userId, accessToken, refreshToken)

        assertEquals(userId, repository.getUserId())
        assertEquals(accessToken, repository.getAccessToken())
        assertEquals(refreshToken, repository.getRefreshToken())
    }

    @Test
    fun `GIVEN accessToken saved WHEN getAccessToken called THEN then accessToken returned`() {
        repository.save(userId, accessToken, refreshToken)
        assertEquals(accessToken, repository.getAccessToken())
    }

    @Test
    fun `GIVEN accessToken not saved WHEN getAccessToken called THEN then null returned`() {
        assertEquals(null, repository.getAccessToken())
    }

    @Test
    fun `GIVEN refreshToken saved WHEN getRefreshToken called THEN then refreshToken returned`() {
        repository.save(userId, accessToken, refreshToken)
        assertEquals(refreshToken, repository.getRefreshToken())
    }

    @Test
    fun `GIVEN refreshToken not saved WHEN getRefreshToken called THEN then null returned`() {
        assertEquals(null, repository.getAccessToken())
    }

    @Test
    fun `GIVEN userId saved WHEN getUserId called THEN then userId returned`() {
        repository.save(userId, accessToken, refreshToken)
        assertEquals(userId, repository.getUserId())
    }

    @Test
    fun `GIVEN userId not saved WHEN getUserId called THEN then null returned`() {
        assertEquals(null, repository.getUserId())
    }

    @Test
    fun `WHEN clear called THEN then preferences values cleared`() {
        repository.save(userId, accessToken, refreshToken)

        assertEquals(userId, repository.getUserId())
        assertEquals(accessToken, repository.getAccessToken())
        assertEquals(refreshToken, repository.getRefreshToken())

        repository.clear()

        assertNotEquals(userId, repository.getUserId())
        assertNotEquals(accessToken, repository.getAccessToken())
        assertNotEquals(refreshToken, repository.getRefreshToken())
    }
}
