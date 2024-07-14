package com.shelazh.myclass.data.repository

import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.local.UserDao
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao): UserRepository {
    override suspend fun searchFriend(keyword: String?): List<User> {
        return userDao.searchUser(keyword)
    }

    override suspend fun saveUSer(user: User) {
        val userRegister = user.copy(databaseId = 1)
        userDao.insert(userRegister)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

    override suspend fun checkLogin(): Boolean {
        val isLogin = userDao.checkLogin()
        return isLogin != null
    }

    override suspend fun getUser(): User? {
        return userDao.checkLogin()
    }
}
