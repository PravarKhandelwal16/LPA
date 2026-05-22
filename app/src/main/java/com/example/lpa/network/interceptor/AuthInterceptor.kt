package com.example.lpa.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor for adding authentication headers to outgoing SM-DP+ requests.
 *
 * **Note:** Placeholder for future implementation.
 * SM-DP+ authentication typically involves mutual TLS (mTLS) or specific
 * header signatures rather than simple Bearer tokens. This interceptor
 * will handle injecting the appropriate credentials when implemented.
 */
@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Placeholder: Inject authentication headers
        // request = request.newBuilder()
        //     .addHeader("Authorization", "Bearer token_here")
        //     .build()

        return chain.proceed(request)
    }
}
