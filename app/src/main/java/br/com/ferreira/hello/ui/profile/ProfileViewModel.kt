package br.com.ferreira.hello.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.Profile
import br.com.ferreira.hello.data.model.UserInfo
import br.com.ferreira.hello.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val userId = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val fullname = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun updateData() = authRepository.getuserInfo(object : ApiCallback<UserInfo> {
        override fun onSuccess(value: UserInfo) {
            userId.value = value.id
            username.value = value.userName
            fullname.value = value.fullName
            email.value = value.email
        }

        override fun onError(text: String) {
            message.value = text
        }
    })
}