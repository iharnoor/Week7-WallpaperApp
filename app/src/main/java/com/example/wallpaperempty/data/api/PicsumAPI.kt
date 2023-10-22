package com.example.wallpaperempty.data.api

import com.example.wallpaperempty.data.api.model.ApiPicsumItem
import com.example.wallpaperempty.utils.Constants.PISCUM_END_POINT
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumAPI {
    @GET(PISCUM_END_POINT)
    suspend fun getWallpaperImages(
        @Query("page") page: Int, @Query("limit") limit: Int = 100
    ): List<ApiPicsumItem>?
}