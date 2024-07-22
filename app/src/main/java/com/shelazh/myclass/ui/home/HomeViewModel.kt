package com.shelazh.myclass.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.remote.FriendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel(){

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }
    val user = MutableSharedFlow<List<User>>()
    private val _friendResponse = MutableSharedFlow<FriendResponse>()
    val friendResponse = _friendResponse.asSharedFlow()

    fun getFriendByQuery(keyword: String) = viewModelScope.launch {
        ApiObserver.run({apiService.getFriend()}, false, object : ApiObserver.ResponseListenerFlow<FriendResponse>(_friendResponse){
            override suspend fun onSuccess(response: FriendResponse) {
                val filterData = response.data.filter { it.name == keyword }
                val filterResponse = FriendResponse(filterData)
                _friendResponse.emit(filterResponse)
            }
        })
    }

    fun listFriend() = viewModelScope.launch {
        ApiObserver.run({ apiService.getFriend()}, false, object : ApiObserver.ResponseListenerFlow<FriendResponse>(_friendResponse){
            override suspend fun onSuccess(response: FriendResponse) {
                _friendResponse.emit(response)
            }
        })
    }

    suspend fun getId() = userRepositoryImpl.getUser()?.id

}