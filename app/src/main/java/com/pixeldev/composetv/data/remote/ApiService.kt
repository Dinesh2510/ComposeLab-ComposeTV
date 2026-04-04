package com.pixeldev.composetv.data.remote



import com.pixeldev.composetv.data.model.VideoResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class ApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getVideos(): VideoResponseDto {
        return client.get(
            "/androiddevelopers/samples_assets/android-tv/android_tv_videos_new.json"
        ).body()
    }
}