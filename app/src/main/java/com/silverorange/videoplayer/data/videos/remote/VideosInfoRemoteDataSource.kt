package com.silverorange.videoplayer.data.videos.remote

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto

interface VideosInfoRemoteDataSource {
    suspend fun getAvailableVideos(): List<VideoInfoDto>
}