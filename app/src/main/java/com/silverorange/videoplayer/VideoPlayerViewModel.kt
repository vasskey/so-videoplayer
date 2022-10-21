package com.silverorange.videoplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.silverorange.videoplayer.data.videos.VideosInfoRepository
import com.silverorange.videoplayer.model.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videosInfoRepository: VideosInfoRepository,
    val player: ExoPlayer,
    private val mediaSourceFactory: MediaSource.Factory
) :
    ViewModel(), Player.Listener {
    private lateinit var availableVideos: List<VideoInfo>

    var state by mutableStateOf<DataState<VideoInfo>>(DataState.Loading())

    init {
        loadVideos()
    }

    fun loadVideos(){
        viewModelScope.launch {
            try {
                availableVideos = videosInfoRepository.getAvailableVideosSortedByPublishedDate()
                state = DataState.Ready(availableVideos.first())

                val mediaSources = availableVideos.map {
                    mediaSourceFactory.createMediaSource(
                        MediaItem.fromUri(it.fullURL)
                    )
                }
                player.setMediaSources(mediaSources)
                player.prepare()
                player.addListener(this@VideoPlayerViewModel)
                player.play()
            } catch (e: Exception) {
                state = DataState.Error(e)
            }
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        state = DataState.Ready(availableVideos[player.currentMediaItemIndex])
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(this)
    }
}