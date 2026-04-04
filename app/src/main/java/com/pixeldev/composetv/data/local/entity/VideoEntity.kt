package com.pixeldev.composetv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val title: String,

    val description: String,
    val videoUrl: String,
    val card: String,
    val background: String,
    val studio: String,
    val category: String,

    val isWishlist: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)