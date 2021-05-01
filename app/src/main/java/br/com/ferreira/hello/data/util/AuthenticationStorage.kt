package br.com.ferreira.hello.data.util

import android.content.Context
import android.content.SharedPreferences
import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthenticationStorage @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

    var tokenType: String
        get() = sharedPreferences.getString(AUTH_TOKEN_TYPE, "").orEmpty()
        set(value) = sharedPreferences.putString(AUTH_TOKEN_TYPE, value)

    var accessToken: String
        get() = sharedPreferences.getString(AUTH_TOKEN, "").orEmpty()
        set(value) = sharedPreferences.putString(AUTH_TOKEN, value)

    var refreshToken: String
        get() = sharedPreferences.getString(REFRESH_TOKEN, "").orEmpty()
        set(value) = sharedPreferences.putString(REFRESH_TOKEN, value)

    var userId: String
        get() = sharedPreferences.getString(USER_ID, "").orEmpty()
        set(value) = sharedPreferences.putString(USER_ID, value)

    var username: String
        get() = sharedPreferences.getString(USERNAME, "").orEmpty()
        set(value) = sharedPreferences.putString(USERNAME, value)

    var fullname: String
        get() = sharedPreferences.getString(FULLNAME, "").orEmpty()
        set(value) = sharedPreferences.putString(FULLNAME, value)

    var email: String
        get() = sharedPreferences.getString(EMAIL, "").orEmpty()
        set(value) = sharedPreferences.putString(EMAIL, value)

    fun save(authToken: AuthToken) {
        tokenType = authToken.tokenType
        accessToken = authToken.accessToken
        refreshToken = authToken.refreshToken
    }

    fun save(userInfo: UserInfo) {
        userId = userInfo.id
        username = userInfo.userName
        fullname = userInfo.fullName
        email = userInfo.email
    }

    fun clear() = sharedPreferences.edit()
        .remove(AUTH_TOKEN)
        .remove(REFRESH_TOKEN)
        .remove(AUTH_TOKEN_TYPE)
        .remove(USER_ID)
        .remove(USERNAME)
        .remove(FULLNAME)
        .remove(EMAIL)
        .apply()

    companion object {
        const val PREFERENCES_KEY = "preferences"
        private const val AUTH_TOKEN = "authentication_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val AUTH_TOKEN_TYPE = "auth_token_type"
        private const val USER_ID = "user_id"
        private const val USERNAME = "username"
        private const val FULLNAME = "fullname"
        private const val EMAIL = "email"

        private fun SharedPreferences.putString(key: String, value: String) =
            edit().putString(key, value).apply()
    }
}