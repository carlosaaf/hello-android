package br.com.ferreira.hello.data.repo

import br.com.ferreira.hello.data.client.AuthApiService
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthApiService
) {
    private lateinit var authToken: AuthToken

    fun exchangeCode(code: String, state: String, callback: ApiCallback<AuthToken>) {
        authService.exchangeCode(code, state).enqueue(object : Callback<AuthToken?> {
            override fun onResponse(call: Call<AuthToken?>, response: Response<AuthToken?>) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let { value ->
                            authToken = value
                            callback.onSuccess(value)
                        }
                    }
                    response.code() == 404 -> {
                        callback.onError("Not found")
                    }
                    else -> {
                        callback.onError("Unknown error ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<AuthToken?>, t: Throwable) {
                t.message?.let(callback::onError)
            }
        })
    }

    fun getuserInfo(callback: ApiCallback<UserInfo>) {
        authService.getUserInfo(authToken.tokenType + " " + authToken.accessToken).enqueue(object : Callback<UserInfo?> {
            override fun onResponse(call: Call<UserInfo?>, response: Response<UserInfo?>) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let { value ->
                            callback.onSuccess(value)
                        }
                    }
                    response.code() == 404 -> {
                        callback.onError("Not found")
                    }
                    else -> {
                        callback.onError("Unknown error ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                t.message?.let(callback::onError)
            }
        })
    }
}