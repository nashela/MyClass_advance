package com.shelazh.myclass.ui.home

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.local.UserDao
import com.shelazh.myclass.data.remote.FriendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

//    private val _listFriend = MutableSharedFlow<List<FriendResponse>>()
//    val listFriend = _listFriend

    private val _friendResponse = MutableSharedFlow<FriendResponse>()
    val friendResponse = _friendResponse.asSharedFlow()

    fun getFriendByQuery(keyword: String) = viewModelScope.launch {
        ApiObserver.run({ apiService.getFriend() }, false, object : ApiObserver.ResponseListenerFlow<FriendResponse>(_friendResponse) {
            override suspend fun onSuccess(response: FriendResponse) {
                val filterData = response.data.filter { it.name!!.contains(keyword, ignoreCase = true) }
                val filterResponse = FriendResponse(filterData)
                _friendResponse.emit(filterResponse)
            }
        })
    }

    fun listFriend() = viewModelScope.launch {
        ApiObserver.run({ apiService.getFriend() }, false, object : ApiObserver.ResponseListenerFlow<FriendResponse>(_friendResponse) {
            override suspend fun onSuccess(response: FriendResponse) {
                _friendResponse.emit(response)
            }
        })
    }

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
        }
    }
}
