package com.pixeldev.composetv.di

import android.content.Context
import androidx.room.Room
import com.pixeldev.composetv.data.db.AppDatabase
import com.pixeldev.composetv.data.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "video_db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): VideoDao = db.videoDao()
}