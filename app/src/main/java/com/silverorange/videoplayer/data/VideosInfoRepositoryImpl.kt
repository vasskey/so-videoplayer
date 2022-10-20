package com.silverorange.videoplayer.data

import com.silverorange.videoplayer.data.videos.VideosInfoRepository
import com.silverorange.videoplayer.data.videos.mappers.VideosInfoMapper
import com.silverorange.videoplayer.data.videos.remote.VideosInfoRemoteDataSource
import com.silverorange.videoplayer.model.VideoInfo
import javax.inject.Inject

class VideosInfoRepositoryImpl @Inject constructor(
    private val remoteDataSource: VideosInfoRemoteDataSource,
    private val mapper: VideosInfoMapper
) : VideosInfoRepository {
    override suspend fun getAvailableVideosSortedByPublishedDate(): List<VideoInfo> {
        val videos = remoteDataSource.getAvailableVideos()
        return videos.sortedBy { it.publishedAt }.map { mapper.mapDtoToModel(it) }
    }
}