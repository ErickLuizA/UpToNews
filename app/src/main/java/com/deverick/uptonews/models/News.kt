package com.deverick.uptonews.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val author: String? = null,
    val image: String? = null,
    val language: String? = null,
    val category: List<String>? = null,
    val published: String? = null,
) : Parcelable