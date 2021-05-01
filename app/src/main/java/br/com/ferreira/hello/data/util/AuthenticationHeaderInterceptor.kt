package br.com.ferreira.hello.data.util

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthenticationHeaderInterceptor @Inject constructor(
    private val authenticationLocalStorage: AuthenticationStorage
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header(
                    AUTHORIZATION_KEY,
                    "${authenticationLocalStorage.tokenType} ${authenticationLocalStorage.accessToken}"
                )
                .build()
        )

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}