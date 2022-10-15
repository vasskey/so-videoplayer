package com.silverorange.videoplayer.data.videos.mappers

import com.silverorange.videoplayer.data.videos.dtos.VideoInfoDto
import com.silverorange.videoplayer.model.VideoInfo

class VideosInfoMapperImpl : VideosInfoMapper {
    override fun mapDtoToModel(videoInfoDto: VideoInfoDto): VideoInfo {
        return VideoInfo(
            id = videoInfoDto.id,
            title = videoInfoDto.title,
            fullURL = videoInfoDto.fullURL,
            description = videoInfoDto.description,
            authorName = videoInfoDto.author.name
        )
    }
}