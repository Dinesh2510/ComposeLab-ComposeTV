package com.pixeldev.composetv.domain.model

data class Video(
    val title: String,
    val description: String,
    val videoUrl: String,
    val image: String,
    val background: String,
    val studio: String,
    val isWishlist: Boolean = false   // ✅ ADD THIS

)

data class Category(
    val name: String,
    val videos: List<Video>
)