package com.shelazh.myclass.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolModel (
    @Expose
    @SerializedName("id")
    val id: Int?,

    @Expose
    @SerializedName("school_name")
    val school: String?,
):Parcelable