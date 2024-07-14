package com.shelazh.myclass.di

import android.content.Context
import androidx.room.Database
import com.crocodic.core.helper.NetworkHelper
import com.shelazh.myclass.api.ApiService
import com.shelazh.myclass.data.MyDatabase
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = MyDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideUserDao(database: MyDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideApiService(): ApiService{
        return NetworkHelper.provideApiService(
            baseUrl = "http://kelas-industri.crocodic.net/rubben/kelasku/public/api/v1/auth/",
            okHttpClient = NetworkHelper.provideOkHttpClient(),
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class RepositoryModule{
        @Singleton
        @Binds
        abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
    }
}