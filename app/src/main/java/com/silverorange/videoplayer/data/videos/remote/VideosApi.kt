package com.silverorange.videoplayer.data.videos.remote

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto
import retrofit2.http.GET

interface VideosApi {
    @GET("/videos")
    suspend fun getVideos() : List<VideoInfoDto>
}