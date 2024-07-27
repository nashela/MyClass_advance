package com.shelazh.myclass.api

import com.shelazh.myclass.data.remote.FriendResponse
import com.shelazh.myclass.data.remote.LoginResponse
import com.shelazh.myclass.data.remote.ProfileResponse
import com.shelazh.myclass.data.remote.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
//    @GET("user")
//    suspend fun getToken(): String

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("phone") phone: String?,
        @Field("name") name: String?,
        @Field("school_id") schoolId: Int?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?
    ): RegisterResponse

    @FormUrlEncoded
    @POST("profile/update")
    suspend fun updateProfileNoPhoto(
        @Field("id") id: Int?,
        @Field("phone") phone: String?,
        @Field("name") name: String?,
//        @Field("school_id") schoolId: Int?
    ): ProfileResponse

    @Multipart
    @POST("profile/update")
    suspend fun updateProfileWithPhoto(
        @Part("id") id: Int?,
        @Part("name") name: String?,
//        @Part("school_id") schoolId: String,
        @Part("phone") phone: String?,
        @Part photo: MultipartBody.Part?
    ): ProfileResponse

    @GET("friend/find")
    suspend fun getFriend(): FriendResponse
}
