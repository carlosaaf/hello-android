package br.com.ferreira.hello.data.util

import br.com.ferreira.hello.data.client.AuthApiService
import br.com.ferreira.hello.data.model.AuthToken
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import javax.inject.Inject

class OAuth2Authenticator @Inject constructor(
    private val authApiService: AuthApiService,
    private val authenticationLocalStorage: AuthenticationStorage
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            val refreshTokenResponse =
                authApiService.refreshToken(authenticationLocalStorage.refreshToken).execute()
            val authToken: AuthToken? = refreshTokenResponse.body()
            if (refreshTokenResponse.isSuccessful && authToken != null) {
                authenticationLocalStorage.save(authToken)
                return response.request.newBuilder()
                    .header(
                        AUTHORIZATION_KEY,
                        "${authenticationLocalStorage.tokenType} ${authenticationLocalStorage.accessToken}"
                    )
                    .build()
            } else {
                throw HttpException(refreshTokenResponse)
            }
        } catch (throwable: Throwable) {
            return null
        }
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}