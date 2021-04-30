package br.com.ferreira.hello.data.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("sub") var id: String,
    @SerializedName("email_verified") var emailVerified: Boolean,
    @SerializedName("name") var fullName: String,
    @SerializedName("preferred_username") var userName: String,
    @SerializedName("given_name") var givenNname: String,
    @SerializedName("family_name") var familyName: String,
    @SerializedName("email") var email: String
)
