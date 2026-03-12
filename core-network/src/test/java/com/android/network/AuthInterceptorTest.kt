package com.android.network

import com.android.data.data.EncryptedTokenRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Invocation

class AuthInterceptorTest {

    private val tokenRepository: EncryptedTokenRepository = mockk()
    private val chain: Interceptor.Chain = mockk()
    private lateinit var interceptor: AuthInterceptor

    @Before
    fun setup() {
        interceptor = AuthInterceptor(tokenRepository)
        
        // Default response for chain.proceed
        every { chain.proceed(any()) } returns Response.Builder()
            .request(Request.Builder().url("https://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
    }

    @Test
    fun `GIVEN no NoAuth annotation and access token found exists WHEN request intercepted THEN authorization header is added`() {
        val token = "fake_jwt_token"
        val originalRequest = Request.Builder().url("https://test.com/api/notes").build()
        val capturedRequest = slot<Request>()

        every { tokenRepository.getAccessToken() } returns token
        every { chain.request() } returns originalRequest

        interceptor.intercept(chain)

        verify { chain.proceed(capture(capturedRequest)) }
        val headerValue = capturedRequest.captured.header("Authorization")
        assertEquals("Bearer $token", headerValue)
    }

    @Test
    fun `GIVEN no NoAuth annotation but no access token found WHEN request intercepted THEN no authorization header is added`() {
        val originalRequest = Request.Builder().url("https://test.com/api/notes").build()
        val capturedRequest = slot<Request>()

        every { tokenRepository.getAccessToken() } returns null
        every { chain.request() } returns originalRequest

        interceptor.intercept(chain)

        verify { chain.proceed(capture(capturedRequest)) }
        val headerValue = capturedRequest.captured.header("Authorization")
        assertEquals(null, headerValue)
    }

    @Test
    fun `GIVEN NoAuth annotation WHEN request intercepted THEN no authorization header is added`() {
        val token = "fake_jwt_token"

        val method = TestService::class.java.getDeclaredMethod("noAuthMethod")
        val invocation = Invocation.of(method, emptyList<Any>())

        val originalRequest = Request.Builder()
            .url("https://test.com/api/auth/login")
            .tag(Invocation::class.java, invocation)
            .build()
            
        val capturedRequest = slot<Request>()

        every { tokenRepository.getAccessToken() } returns token
        every { chain.request() } returns originalRequest

        interceptor.intercept(chain)
        
        verify { chain.proceed(capture(capturedRequest)) }
        val headerValue = capturedRequest.captured.header("Authorization")
        assertEquals(null, headerValue)
    }

    private interface TestService {
        @NoAuth
        fun noAuthMethod()
    }
}
