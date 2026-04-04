package com.pixeldev.composetv.data.repository

import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.dao.VideoDao
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.mapper.toEntityList
import com.pixeldev.composetv.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
class VideoRepositoryImpl(
    private val api: ApiService,
    private val dao: VideoDao
) : VideoRepository {

    override fun fetchAndStoreVideos(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getVideos()

            val existing = dao.getAllVideos().first()

            val mapped = response.toEntityList(existing)

            dao.clearAll()
            dao.insertAll(mapped)

            emit(ResultState.Success(Unit))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Something went wrong"))
        }
    }

    override fun getVideos(): Flow<List<VideoEntity>> {
        return dao.getAllVideos()
    }

    override fun getWishlist(): Flow<List<VideoEntity>> {
        return dao.getWishlist()
    }

    override suspend fun toggleWishlist(title: String, isWish: Boolean) {
        dao.updateWishlist(title, isWish)
    }
}