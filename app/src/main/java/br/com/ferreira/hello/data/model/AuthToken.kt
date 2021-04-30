package br.com.ferreira.hello.data.model

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("expires_in") var expiresIn: Long,
    @SerializedName("refresh_expires_in") var refreshExpiresIn: Long,
    @SerializedName("refresh_token") var refreshToken: String,
    @SerializedName("token_type") var tokenType: String,
    @SerializedName("id_token") var idToken: String,
    @SerializedName("not-before-policy") var notBeforePolicy: Long,
    @SerializedName("session_state") var sessionState: String,
    @SerializedName("scope") var scope: String
)