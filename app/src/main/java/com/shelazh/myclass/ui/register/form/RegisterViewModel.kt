package com.shelazh.myclass.ui.register.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.remote.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : BaseViewModel(){
    private val _registerResponse = MutableSharedFlow<RegisterResponse>()
    val registerResponse = _registerResponse.asSharedFlow()

    val message = MutableLiveData<String>()

    fun register(phone: String?, name: String?, schoolId: Int?, password: String?, passwordConfirmation: String?) =
        viewModelScope.launch {
            try {
                ApiObserver.run(
                    { apiService.register(phone, name, schoolId, password, passwordConfirmation) },
                    false,
                    object : ApiObserver.ResponseListenerFlow<RegisterResponse>(_registerResponse){
                        override suspend fun onSuccess(response: RegisterResponse) {
                            _registerResponse.emit(response)
                        }
                    }
                    )
            }catch (e : Exception){
                val error = "The phone has already been taken"
                message.value = error
            }
        }
}