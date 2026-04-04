package com.pixeldev.composetv.data.model
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponseDto(
    val googlevideos: List<CategoryDto>
)

@Serializable
data class CategoryDto(
    val category: String,
    val videos: List<VideoDto>
)

@Serializable
data class VideoDto(
    val title: String,
    val description: String,
    val sources: List<String>,
    val card: String,
    val background: String,
    val studio: String
)