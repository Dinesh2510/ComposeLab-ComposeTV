package com.pixeldev.composetv.data.repository

import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.dao.VideoDao
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.mapper.toEntityList
import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
interface VideoRepository {

    fun fetchAndStoreVideos(): Flow<ResultState<Unit>>

    fun getVideos(): Flow<List<VideoEntity>>

    fun getWishlist(): Flow<List<VideoEntity>>

    suspend fun toggleWishlist(title: String, isWish: Boolean)
}