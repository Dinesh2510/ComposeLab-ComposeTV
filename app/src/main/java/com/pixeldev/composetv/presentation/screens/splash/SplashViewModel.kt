package com.pixeldev.composetv.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.DataStoreManager
import com.pixeldev.composetv.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: VideoRepository
) : ViewModel() {

    val onboardingDone = dataStoreManager.onboardingDone

    private val _syncState =
        MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val syncState: StateFlow<ResultState<Unit>> = _syncState

    // ✅ 1. Prevent multiple API calls
    private var hasSynced = false

    init {
        syncVideos()
    }

    private fun syncVideos() {
        if (hasSynced) return   // 🔥 LOGIC 1 HERE
        hasSynced = true

        viewModelScope.launch {
            repository.fetchAndStoreVideos().collect { result ->
                _syncState.value = result
            }
        }
    }

    // ✅ 2. Retry Logic
    fun retry() {
        hasSynced = false       // reset flag
        syncVideos()            // 🔥 LOGIC 2 HERE
    }
}