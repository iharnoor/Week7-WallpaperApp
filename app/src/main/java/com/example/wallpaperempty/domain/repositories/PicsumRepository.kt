package com.example.wallpaperempty.domain.repositories

import com.example.wallpaperempty.utils.Resource
import com.example.wallpaperempty.domain.entities.PicsumWallpaper
import kotlinx.coroutines.flow.Flow

interface PicsumRepository {
    fun getPicsumImages(): Flow<Resource<List<PicsumWallpaper>>>
}