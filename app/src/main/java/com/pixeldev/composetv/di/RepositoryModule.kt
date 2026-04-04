package com.pixeldev.composetv.di

import com.pixeldev.composetv.data.local.dao.VideoDao
import com.pixeldev.composetv.data.remote.ApiService
import com.pixeldev.composetv.data.repository.VideoRepository
import com.pixeldev.composetv.data.repository.VideoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        api: ApiService,
        dao: VideoDao
    ): VideoRepository {
        return VideoRepositoryImpl(api, dao)
    }
}