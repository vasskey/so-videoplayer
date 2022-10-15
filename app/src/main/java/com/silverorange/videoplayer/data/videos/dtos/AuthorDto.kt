package com.silverorange.videoplayer.data.videos.dtos

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)