package com.silverorange.videoplayer.data.videos.remote

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto
import com.silverorange.videoplayer.exceptions.ApiResponseException
import javax.inject.Inject

class VideosInfoRemoteDataSourceImpl @Inject constructor(private val videosApi: VideosApi) : VideosInfoRemoteDataSource {
    override suspend fun getAvailableVideos(): List<VideoInfoDto> {
        val videosResponse = videosApi.getVideos()
        if (videosResponse.isSuccessful)
            return videosResponse.body()
        else
            throw ApiResponseException()
    }
}