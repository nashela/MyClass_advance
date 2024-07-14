package com.shelazh.myclass.api

import com.shelazh.myclass.data.remote.LoginResponse
import com.shelazh.myclass.data.remote.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("phone") phone : String?,
        @Field("password") password : String?
    ):LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("phone") phone: String?,
        @Field("name") name: String?,
        @Field("school_id") schoolId: Int?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?
    ):RegisterResponse

    @GET("school-list")
    suspend fun getAllSchool(): String

//    @FormUrlEncoded
//    @POST("update")
//    suspend fun updateProfile(
//        @Field("name") name : String?,
//        @Field("school_id") school: String?,
//        @Field("phone") phone: String?,
//        @Field("password") password: String?,
//        @Pa
//        )
}
