package com.silverorange.videoplayer.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.silverorange.videoplayer.AppConsts
import com.silverorange.videoplayer.data.VideosInfoRepositoryImpl
import com.silverorange.videoplayer.data.videos.VideosInfoRepository
import com.silverorange.videoplayer.data.videos.mappers.VideosInfoMapper
import com.silverorange.videoplayer.data.videos.mappers.VideosInfoMapperImpl
import com.silverorange.videoplayer.data.videos.remote.VideosApi
import com.silverorange.videoplayer.data.videos.remote.VideosInfoRemoteDataSource
import com.silverorange.videoplayer.data.videos.remote.VideosInfoRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
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

    @Singleton
    @Provides
    fun provideVideosInfoMapper(): VideosInfoMapper = VideosInfoMapperImpl()

    @Singleton
    @Provides
    fun provideVideosInfoRepository(
        dataSource: VideosInfoRemoteDataSource,
        mapper: VideosInfoMapper
    ): VideosInfoRepository = VideosInfoRepositoryImpl(dataSource, mapper)

    private fun prepareRetrofitConfiguration(): Retrofit {
        val gsonBuilder = GsonBuilder().registerTypeAdapter(
            LocalDateTime::class.java,
            object : JsonDeserializer<LocalDateTime> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): LocalDateTime {
                    return json?.asString?.let {
                        Instant.parse(it).toLocalDateTime(TimeZone.currentSystemDefault())
                    } ?: Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault())
                }
            }).create()

        return Retrofit.Builder()
            .baseUrl(AppConsts.BASE_API_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
    }
}