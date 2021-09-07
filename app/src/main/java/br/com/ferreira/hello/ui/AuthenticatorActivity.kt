package br.com.ferreira.hello.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import br.com.ferreira.hello.BuildConfig
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import br.com.ferreira.hello.data.repo.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorActivity : AppCompatActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data != null) {
            val data = intent.data!!
            val code = data.getQueryParameter("code")
            val state = data.getQueryParameter("session_state")
            if (code != null && state != null) {
                exchangeCode(code, state)
            }
        } else {
            val uri = Uri.parse(BuildConfig.AUTH_URL + "protocol/openid-connect/auth")
                .buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", "hello-android")
                .appendQueryParameter("redirect_uri", "br.com.ferreira.hello://oauthredirect")
                .appendQueryParameter("scope", "openid email profile")
                .build()
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
                .setShowTitle(false)
                .setUrlBarHidingEnabled(false)
                .build()
            customTabsIntent.launchUrl(this, uri)
        }
    }

    private fun exchangeCode(code: String, state: String) {
        authRepository.exchangeCode(code, state, object : ApiCallback<AuthToken> {
            override fun onSuccess(value: AuthToken) {
                Log.i(TAG, value.accessToken)
                loadUserInfo()
            }

            override fun onError(text: String) {
                Log.e(TAG, text)
            }
        })
    }

    private fun loadUserInfo() {
        authRepository.getuserInfo(object : ApiCallback<UserInfo> {
            override fun onSuccess(value: UserInfo) {
                Log.i(TAG, value.fullName)
            }

            override fun onError(text: String) {
                Log.e(TAG, text)
            }

        })
    }

    companion object {
        private const val TAG = "AuthenticatorActivity"
    }
}