package com.shelazh.myclass.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("id_database")
    val databaseId: Int = 0,

    @Expose
    @SerializedName("id")
    var id: Int?,

    @Expose
    @SerializedName("phone")
    var phone: String?,

    @Expose
    @SerializedName("name")
    var name: String?,

    @Expose
    @SerializedName("school_id")
    var schoolId: Int?,

//    @Expose
//    @SerializedName("password")
//    var password: String?,
//
//    @Expose
//    @SerializedName("password_confirmation")
//    var passwordConfirmation: String?,

//    @Expose
//    @SerializedName("photo")
//    var phone: String?,

//    colek
//    @Expose
//    @SerializedName()
): Parcelable