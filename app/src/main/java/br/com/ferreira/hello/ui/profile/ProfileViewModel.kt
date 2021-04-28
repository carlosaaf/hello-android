package br.com.ferreira.hello.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.repo.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val message = MutableLiveData<String>()

    fun updateMessage() = profileRepository.getMessage(10L, object : ApiCallback {
        override fun onSuccess(text: String) {
            message.value = text
        }

        override fun onError(text: String) {
            message.value = text
        }
    })
}