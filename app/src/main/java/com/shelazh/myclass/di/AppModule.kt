package com.shelazh.myclass.di

import android.content.Context
import androidx.room.Database
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.NetworkHelper
import com.crocodic.core.helper.okhttp.SSLTrust
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
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
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = MyDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideUserDao(database: MyDatabase) = database.userDao()


    @Provides
    fun provideSession(@ApplicationContext context: Context) = CoreSession(context)

    @Singleton
    @Provides
    fun provideApiService(): ApiService{
        return NetworkHelper.provideApiService(
            baseUrl = "https://kelas-industri.crocodic.net/rubben/kelasku/public/api/v1/",
            okHttpClient = NetworkHelper.provideOkHttpClient(),
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }

    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context, session: CoreSession): OkHttpClient {
        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val accessToken = session.getString("token")

                val requestBuilder = original.newBuilder()
                    .header("Authorization", accessToken)
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        return okHttpClient.build()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class RepositoryModule{
        @Singleton
        @Binds
        abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
    }

}