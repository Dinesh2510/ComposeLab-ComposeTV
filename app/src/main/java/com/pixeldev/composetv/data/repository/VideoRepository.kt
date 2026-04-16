package com.pixeldev.composetv.data.repository

import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun fetchAndStoreVideos(): Flow<ResultState<Unit>>

    fun getVideos(): Flow<List<VideoEntity>>

    fun getVideosByCategory(category: String): Flow<List<VideoEntity>>

    fun getVideoByTitle(title: String): Flow<VideoEntity?>

    fun getWishlist(): Flow<List<VideoEntity>>

    suspend fun toggleWishlist(title: String, isWish: Boolean)

    fun searchVideos(query: String): Flow<List<VideoEntity>>

    fun getSearchSuggestions(query: String): Flow<List<String>>

    fun getRandomTitles(): Flow<List<String>>
}