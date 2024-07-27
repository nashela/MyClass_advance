package com.shelazh.myclass.base.viewModel

import android.content.SharedPreferences
import javax.inject.Inject

class CoreSession @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
}