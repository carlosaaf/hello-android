package br.com.ferreira.hello.data.repo

import br.com.ferreira.hello.data.client.AuthApiService
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import br.com.ferreira.hello.data.util.AuthenticationStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthApiService,
    private val authStorage: AuthenticationStorage
) {

    fun getuserInfo(callback: ApiCallback<UserInfo>) =
        authService.getUserInfo(authStorage.tokenType + " " + authStorage.accessToken)
            .enqueue(object : Callback<UserInfo?> {
                override fun onResponse(call: Call<UserInfo?>, response: Response<UserInfo?>) {
                    when {
                        response.isSuccessful -> {
                            response.body()?.let(callback::onSuccess)
                        }
                        response.code() == 404 -> {
                            authStorage.clear()
                            callback.onError("Not found")
                        }
                        else -> {
                            authStorage.clear()
                            callback.onError("Unknown error ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                    authStorage.clear()
                    t.message?.let(callback::onError)
                }
            })

    fun save(userInfo: UserInfo) = authStorage.save(userInfo)

    fun save(authToken: AuthToken) = authStorage.save(authToken)

    fun clear() = authStorage.clear()
}