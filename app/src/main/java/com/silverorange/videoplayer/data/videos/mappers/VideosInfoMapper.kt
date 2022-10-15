package com.silverorange.videoplayer.data.videos.mappers

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto
import com.silverorange.videoplayer.model.VideoInfo

interface VideosInfoMapper {
    fun mapDtoToModel(videoInfoDto: VideoInfoDto): VideoInfo
}