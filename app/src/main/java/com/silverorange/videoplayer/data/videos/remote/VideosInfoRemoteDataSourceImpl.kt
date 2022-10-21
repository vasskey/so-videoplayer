package com.silverorange.videoplayer.data.videos.remote

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideosInfoRemoteDataSourceImpl @Inject constructor(private val videosApi: VideosApi) :
    VideosInfoRemoteDataSource {
    override suspend fun getAvailableVideos(): List<VideoInfoDto> {
        return withContext(Dispatchers.IO) { videosApi.getVideos() }
    }
}