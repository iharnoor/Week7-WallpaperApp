package com.example.wallpaperempty.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.wallpaperempty.utils.DefaultDispatcher
import com.example.wallpaperempty.utils.DispatcherProvider
import com.example.wallpaperempty.data.PicsumRepositoryImp
import com.example.wallpaperempty.data.api.PicsumAPI
import com.example.wallpaperempty.domain.repositories.PicsumRepository
import com.example.wallpaperempty.utils.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        @Provides
        @Singleton
        fun bindDispatcher(): DispatcherProvider = DefaultDispatcher()

        @Provides
        @Singleton
        fun httpClient(@ApplicationContext context: Context) = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()

        @Provides
        @Singleton
        fun bindApi(client: OkHttpClient) = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(Constants.PISCUM_END_POINT)
            .build().create(PicsumAPI::class.java)
    }

    @Binds
    @Singleton
    abstract fun bindPicsumRepository(repositoryImpl: PicsumRepositoryImp): PicsumRepository
}