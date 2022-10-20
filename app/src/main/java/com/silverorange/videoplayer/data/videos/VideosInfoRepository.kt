package com.silverorange.videoplayer.data.videos

import com.silverorange.videoplayer.model.VideoInfo

interface VideosInfoRepository {
    suspend fun getAvailableVideosSortedByPublishedDate(): List<VideoInfo>
}