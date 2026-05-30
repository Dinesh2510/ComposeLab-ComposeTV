package com.pixeldev.composetv.presentation.screens.player // Use your actual package name

import androidx.lifecycle.ViewModel
import com.pixeldev.composetv.data.local.entity.VideoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedPlaybackViewModel : ViewModel() {
    private val _selectedVideo = MutableStateFlow<VideoEntity?>(null)
    val selectedVideo = _selectedVideo.asStateFlow()

    // Call this before navigating to set the video data
    fun setVideo(video: VideoEntity) {
        _selectedVideo.value = video
    }
}