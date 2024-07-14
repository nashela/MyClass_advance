package com.shelazh.myclass.data.repository

import com.shelazh.myclass.data.local.User

interface UserRepository {
    suspend fun saveUSer(user: User)

    suspend fun searchFriend(keyword: String?): List<User>

    suspend fun deleteUser()

    suspend fun checkLogin(): Boolean

    suspend fun getUser(): User?
}