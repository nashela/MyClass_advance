package com.shelazh.myclass.base.viewModel

import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.google.gson.Gson
import com.shelazh.myclass.api.ApiService
import com.shelazh.myclass.data.local.UserDao
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.data.repository.UserRepositoryImpl
import javax.inject.Inject

open class BaseViewModel : CoreViewModel(){

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userRepositoryImpl: UserRepositoryImpl

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var session: com.shelazh.myclass.base.viewModel.CoreSession

    @Inject
    lateinit var gson: Gson


    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }
}