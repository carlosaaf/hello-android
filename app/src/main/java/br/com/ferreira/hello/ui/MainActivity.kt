package br.com.ferreira.hello.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import br.com.ferreira.hello.R
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.UserInfo
import br.com.ferreira.hello.data.repo.AuthRepository
import br.com.ferreira.hello.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        authRepository.getuserInfo(object : ApiCallback<UserInfo> {
            override fun onSuccess(value: UserInfo) {
                navController.navigate(R.id.profileFragment)
            }

            override fun onError(text: String) {
                startActivity(Intent(this@MainActivity, AuthenticatorActivity::class.java))
            }

        })
    }
}