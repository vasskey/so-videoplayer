package com.silverorange.videoplayer.di

import com.silverorange.videoplayer.AppConsts
import com.silverorange.videoplayer.data.videos.remote.VideosApi
import com.silverorange.videoplayer.data.videos.remote.VideosInfoRemoteDataSource
import com.silverorange.videoplayer.data.videos.remote.VideosInfoRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //Since it's really small app I will use only one module for the project.
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = prepareRetrofitConfiguration()

    @Singleton
    @Provides
    fun provideVideosApi(retrofit: Retrofit): VideosApi = retrofit.create(VideosApi::class.java)

    @Singleton
    @Provides
    fun provideVideosRemoteDataSource(videosApi: VideosApi): VideosInfoRemoteDataSource =
        VideosInfoRemoteDataSourceImpl(videosApi)

    private fun prepareRetrofitConfiguration(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConsts.BASE_API_URL)
            .build()
    }
}