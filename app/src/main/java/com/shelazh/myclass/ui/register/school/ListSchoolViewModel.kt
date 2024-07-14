package com.shelazh.myclass.ui.register.school

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.shelazh.myclass.base.viewModel.BaseViewModel
import com.shelazh.myclass.data.SchoolModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSchoolViewModel @Inject constructor() : BaseViewModel() {
    val dataListSchool = MutableLiveData<List<SchoolModel?>>()

    fun getDataSchool(){
        viewModelScope.launch {
            ApiObserver.run { apiService.getAllSchool() }
//            _apiResponse.send()
        }
    }
}