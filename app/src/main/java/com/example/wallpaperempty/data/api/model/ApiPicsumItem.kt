package com.example.wallpaperempty.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiPicsumItem(
    val author: String?,
    @SerializedName("download_url")
    val downloadUrl: String?,
    val height: Int?,
    val id: String?,
    val url: String?,
    val width: Int?
)