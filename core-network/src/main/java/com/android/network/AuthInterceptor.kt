package com.android.network

import com.android.data.data.EncryptedTokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val tokenManager: EncryptedTokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Check if the endpoint has the @NoAuth annotation
        val invocation = request.tag(Invocation::class.java)
        val noAuthAnnotation = invocation?.method()?.getAnnotation(NoAuth::class.java)

        // If it has @NoAuth, proceed without adding the header
        if (noAuthAnnotation != null) {
            return chain.proceed(request)
        }

        // Get token from your local storage
        val token = tokenManager.getAccessToken()

        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}