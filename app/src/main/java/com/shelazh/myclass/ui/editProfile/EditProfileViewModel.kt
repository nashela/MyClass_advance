package com.shelazh.myclass.ui.editProfile

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.remote.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : BaseViewModel() {

    suspend fun getUser() = userRepository.getUser()

    private val _updateProfileResponse = MutableSharedFlow<ProfileResponse>()
    val updateProfileResponse = _updateProfileResponse.asSharedFlow()

    fun updateProfileName(name: String, phone: String) {
        viewModelScope.launch {
            ApiObserver.run(
                { apiService.updateProfileNoPhoto(getUser()?.id ?: 0, name, phone) },
                false,
                object : ApiObserver.ResponseListenerFlow<ProfileResponse>(_updateProfileResponse) {
                    override suspend fun onSuccess(response: ProfileResponse) {
                        response.data?.let {
                            userRepository.saveUSer(it.copy(databaseId = 1))
                        }
                        _updateProfileResponse.emit(response)
                    }
                }
            )
        }
    }

    fun updateProfile(name: String, phone: String, photo: File) {
        val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("photo", photo.name, fileBody)
        viewModelScope.launch {
            ApiObserver.run(
                { apiService.updateProfileWithPhoto(getUser()?.id ?: 0, name, phone, filePart) },
                false,
                object : ApiObserver.ResponseListenerFlow<ProfileResponse>(_updateProfileResponse) {
                    override suspend fun onSuccess(response: ProfileResponse) {
                        response.data?.let {
                            userRepository.saveUSer(it.copy(databaseId = 1))
                        }
                        _updateProfileResponse.emit(response)
                    }
                }
            )
        }
    }
}