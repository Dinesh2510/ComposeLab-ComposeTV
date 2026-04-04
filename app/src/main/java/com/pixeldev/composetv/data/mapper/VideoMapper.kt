package com.pixeldev.composetv.data.mapper


import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.model.*
import com.pixeldev.composetv.domain.model.*
fun VideoResponseDto.toEntityList(
    existing: List<VideoEntity>
): List<VideoEntity> {

    val wishlistMap = existing.associateBy { it.title }

    return googlevideos.flatMap { category ->
        category.videos.map { video ->

            val existingVideo = wishlistMap[video.title]

            VideoEntity(
                title = video.title,
                description = video.description,
                videoUrl = video.sources.firstOrNull() ?: "",
                card = video.card,
                background = video.background,
                studio = video.studio,
                category = category.category,
                isWishlist = existingVideo?.isWishlist ?: false
            )
        }
    }
}