package br.com.ferreira.hello.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import br.com.ferreira.hello.BuildConfig
import br.com.ferreira.hello.data.client.AuthApiService
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.AuthToken
import br.com.ferreira.hello.data.model.UserInfo
import br.com.ferreira.hello.data.repo.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var authService: AuthApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Authentication"
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


    fun exchangeCode(code: String, state: String) =
        authService.exchangeCode(code, state).enqueue(object : Callback<AuthToken?> {
            override fun onResponse(call: Call<AuthToken?>, response: Response<AuthToken?>) {
                when {
                    response.isSuccessful -> response.body()?.let { value ->
                        authRepository.save(value)
                        loadUserInfo()
                    }
                    response.code() == 404 -> {
                        authRepository.clear()
                        Toast.makeText(
                            this@AuthenticatorActivity,
                            "Not found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        authRepository.clear()
                        Toast.makeText(
                            this@AuthenticatorActivity,
                            "Unknown error ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthToken?>, t: Throwable) {
                t.message?.let { text ->
                    Toast.makeText(this@AuthenticatorActivity, text, Toast.LENGTH_LONG).show()
                }
            }
        })


    private fun loadUserInfo() {
        authRepository.getuserInfo(object : ApiCallback<UserInfo> {
            override fun onSuccess(value: UserInfo) {
                authRepository.save(value)
                startActivity(Intent(this@AuthenticatorActivity, MainActivity::class.java))
                finish()
            }

            override fun onError(text: String) {
                Toast.makeText(this@AuthenticatorActivity, text, Toast.LENGTH_LONG).show()
            }
        })
    }
}