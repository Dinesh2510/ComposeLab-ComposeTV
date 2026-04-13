package com.pixeldev.composetv.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.pixeldev.composetv.data.local.entity.VideoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _selectedVideo = MutableStateFlow<VideoEntity?>(null)
    val selectedVideo = _selectedVideo.asStateFlow()

    fun selectVideo(video: VideoEntity) {
        _selectedVideo.value = video
    }
}