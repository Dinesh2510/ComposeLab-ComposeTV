package com.pixeldev.composetv.data.repository

import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.dao.VideoDao
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.mapper.toEntityList
import com.pixeldev.composetv.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: VideoDao
) : VideoRepository {

    override fun fetchAndStoreVideos(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getVideos()
            val existing = dao.getAllVideos().first()

            val mapped = response.toEntityList(existing)
           // dao.clearAll()
            dao.insertAll(mapped)
            emit(ResultState.Success(Unit))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Something went wrong"))
        }
    }

    override fun getVideos(): Flow<List<VideoEntity>> {
        return dao.getAllVideos()
    }

    override fun getVideosByCategory(category: String): Flow<List<VideoEntity>> {
        return dao.getVideosByCategory(category)
    }

    override fun getVideoByTitle(title: String): Flow<VideoEntity?> {
        return dao.getVideoByTitle(title)
    }

    override fun getWishlist(): Flow<List<VideoEntity>> {
        return dao.getWishlist()
    }

    override suspend fun toggleWishlist(title: String, isWish: Boolean) {
        dao.updateWishlist(title, isWish)
    }

    override fun searchVideos(query: String): Flow<List<VideoEntity>> {
        return dao.searchVideos(query)
    }

    override fun getSearchSuggestions(query: String): Flow<List<String>> {
        return dao.getSearchSuggestions(query)
    }
    override fun getRandomTitles(): Flow<List<String>> {
        return dao.getRandomTitles()
    }
}