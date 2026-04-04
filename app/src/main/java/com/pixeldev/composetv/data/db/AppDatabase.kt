package com.pixeldev.composetv.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pixeldev.composetv.data.local.dao.VideoDao
import com.pixeldev.composetv.data.local.entity.VideoEntity

@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}