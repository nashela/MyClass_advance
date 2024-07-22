package com.shelazh.myclass.api

import com.shelazh.myclass.data.remote.FriendResponse
import com.shelazh.myclass.data.remote.LoginResponse
import com.shelazh.myclass.data.remote.ProfileResponse
import com.shelazh.myclass.data.remote.RegisterResponse
import org.json.JSONObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("user")
    suspend fun getToken(): String

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("phone") phone : String?,
        @Field("password") password : String?
    ):LoginResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("phone") phone: String?,
        @Field("name") name: String?,
        @Field("school_id") schoolId: Int?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?
    ):RegisterResponse

    @FormUrlEncoded
    @POST("update-profile")
    suspend fun updateProfileNoPhoto(
        @Field("id") id: Int?,
        @Field("phone") phone: String?,
        @Field("name") name: String?,
        @Field("school_id") schoolId: Int?
    ):ProfileResponse

    @GET("friend")
    suspend fun getFriend(): FriendResponse

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
