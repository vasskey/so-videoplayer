package com.silverorange.videoplayer.data.videos.dtos

import com.google.gson.annotations.SerializedName

data class VideoInfoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("hlsURL")
    val hlsURL: String,

    @SerializedName("fullURL")
    val fullURL: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("author")
    val author: AuthorDto
)
