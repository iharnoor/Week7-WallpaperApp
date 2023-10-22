package com.example.wallpaperempty.data

import com.example.wallpaperempty.utils.Resource
import com.example.wallpaperempty.data.api.PicsumAPI
import com.example.wallpaperempty.domain.entities.PicsumWallpaper
import com.example.wallpaperempty.domain.repositories.PicsumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PicsumRepositoryImp @Inject constructor(
    val api: PicsumAPI
) : PicsumRepository {
    override fun getPicsumImages(): Flow<Resource<List<PicsumWallpaper>>> = flow {
        try {
            val response = api.getWallpaperImages(page = 1)
            response?.let {
                val movies: List<PicsumWallpaper> =
                    it.map { PicsumWallpaper(it.downloadUrl.orEmpty()) }
                emit(Resource.Success(movies))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(null, e.message()))
        } catch (e: IOException) {
            emit(Resource.Error(null, e.message ?: "Network connection Error"))
        }
    }

}