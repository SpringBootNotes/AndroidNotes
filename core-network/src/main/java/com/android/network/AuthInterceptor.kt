package com.android.network

import com.android.data.data.EncryptedTokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val tokenManager: EncryptedTokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        return if (request.noAuthAnnotationFound()) {
            // If request has @NoAuth, proceed without adding the header
            chain.proceed(request)
        } else {
            attachAuthHeader(request, chain)
        }
    }

    private fun Request.noAuthAnnotationFound(): Boolean {
        // Check if the endpoint has the @NoAuth annotation
        val invocation = this.tag(Invocation::class.java)
        val noAuthAnnotation = invocation?.method()?.getAnnotation(NoAuth::class.java)

        return noAuthAnnotation != null
    }

    private fun attachAuthHeader(request: Request, chain: Interceptor.Chain): Response {
        // Get token from your local storage
        val token = tokenManager.getAccessToken()

        val newRequest = token?.let {
            request.newBuilder()
                .addHeader(AUTH_HEADER, "Bearer $token")
                .build()
        } ?: request

        return chain.proceed(newRequest)
    }

    private companion object {
        const val AUTH_HEADER = "Authorization"
    }
}