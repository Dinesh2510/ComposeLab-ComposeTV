package com.pixeldev.composetv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pixeldev.composetv.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoEntity>)

    @Query("SELECT * FROM videos")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE category = :category")
    fun getVideosByCategory(category: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE title = :title LIMIT 1")
    fun getVideoByTitle(title: String): Flow<VideoEntity?>

    @Query("SELECT * FROM videos WHERE isWishlist = 1")
    fun getWishlist(): Flow<List<VideoEntity>>

    @Query("UPDATE videos SET isWishlist = :isWishlist WHERE title = :title")
    suspend fun updateWishlist(title: String, isWishlist: Boolean)

    @Query("DELETE FROM videos")
    suspend fun clearAll()

    @Query("SELECT * FROM videos WHERE title LIKE '%' || :query || '%'")
    fun searchVideos(query: String): Flow<List<VideoEntity>>

    @Query("""
    SELECT DISTINCT title 
    FROM videos 
    WHERE title LIKE :query || '%' 
    LIMIT 10
""")
    fun getSearchSuggestions(query: String): Flow<List<String>>

    @Query("""
    SELECT title 
    FROM videos 
    ORDER BY RANDOM() 
    LIMIT 5
""")
    fun getRandomTitles(): Flow<List<String>>
}