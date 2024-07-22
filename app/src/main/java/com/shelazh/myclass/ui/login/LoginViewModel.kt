package com.shelazh.myclass.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.data.CoreSession
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.remote.FriendResponse
import com.shelazh.myclass.data.remote.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {

    private val _loginResponse = MutableSharedFlow<LoginResponse>()
    val loginResponse = _loginResponse.asSharedFlow()

    private val _tokenResponse = MutableLiveData<ApiResponse>()
    val tokenResponse = _tokenResponse

    override fun apiLogout() {
    }

    override fun apiRenewToken() {
    }

    fun login(phone: String?, password: String?) = viewModelScope.launch {
        ApiObserver.run(
            { apiService.login(phone, password) },
            false,
            object : ApiObserver.ResponseListenerFlow<LoginResponse>(_loginResponse) {
                override suspend fun onSuccess(response: LoginResponse) {
                    super.onSuccess(response)
                    response.data?.let {
                        userRepository.saveUSer(it) // Pastikan nama metode sesuai
                        getToken() // Mendapatkan token setelah login berhasil
                    }
                }
            }
        )
    }

    fun checkLogin(isLogin: (Boolean) -> Unit) {
        viewModelScope.launch {
            val login = userDao.isLogin()
            isLogin(login)
        }
    }

    fun getToken() {
        viewModelScope.launch {
            _tokenResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.getToken() },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val token = response.getString("token")
                        // Simpan token ke session atau SharedPreferences
                        _tokenResponse.postValue(ApiResponse(ApiStatus.SUCCESS, "Token berhasil diambil"))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        _tokenResponse.postValue(ApiResponse(ApiStatus.ERROR, "Gagal mendapatkan token"))
                    }
                }
            )
        }
    }
}
