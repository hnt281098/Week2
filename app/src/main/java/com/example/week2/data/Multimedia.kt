package com.example.week2.data

import com.google.gson.annotations.SerializedName

data class Multimedia(
    val rank: Int,
    val subtype: String,
    val caption: Any,
    val credit: Any,
    val type: String,
    val url: String,
    val height: Int,
    val width: Int,
    val legacy: Legacy,
    val subType: String,
    @SerializedName("crop_name")
    val cropName: String
)