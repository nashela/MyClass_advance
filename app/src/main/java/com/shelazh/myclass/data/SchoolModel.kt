package com.shelazh.myclass.data

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class SchoolModel (
    @Expose
    @SerializedName("id")
    val id: Int?,

    @Expose
    @SerializedName("school_name")
    val schoolName: String?,
):Parcelable