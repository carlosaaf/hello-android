package br.com.ferreira.hello.data.client

import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("protocol/openid-connect/token")
    fun exchangeCode(
        @Field("code") code: String,
        @Field("session_state") state: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String = "hello-android",
        @Field("redirect_uri") redirectUri: String = "br.com.ferreira.hello://oauthredirect"
    ): Call<AuthToken>

    @FormUrlEncoded
    @POST("protocol/openid-connect/token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = "hello-android"
    ): Call<AuthToken>

    @POST("protocol/openid-connect/userinfo")
    fun getUserInfo(
        @Header("Authorization") accessToken: String
    ): Call<UserInfo>
}