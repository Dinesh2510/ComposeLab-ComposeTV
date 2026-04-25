package com.pixeldev.composetv.presentation.screens.home
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.DataStoreManager
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.repository.VideoRepository
import com.pixeldev.composetv.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    // 🔄 Sync State (API → DB)
    private val _syncState =
        MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val syncState: StateFlow<ResultState<Unit>> = _syncState

    // 📺 All Videos (from DB)
    val videos: StateFlow<List<VideoEntity>> =
        repository.getVideos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ❤️ Wishlist (from DB)
    val wishlist: StateFlow<List<VideoEntity>> =
        repository.getWishlist()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val wishlistFlow: Flow<List<VideoEntity>> = repository.getWishlist()

    // 🚀 Init Load
    init {
        fetchVideos()
    }

    // 🔽 API → Room Sync
    fun fetchVideos() {
        viewModelScope.launch {
            repository.fetchAndStoreVideos().collect { result ->
                _syncState.value = result
            }
        }
    }

    // ❤️ Toggle Wishlist
    // this is Done in HomeDetailsViewModel ignore below code
    fun toggleWishlist(video: VideoEntity) {
        viewModelScope.launch {
            repository.toggleWishlist(
                title = video.title,
                isWish = !video.isWishlist
            )
        }
    }

    // 🔄 Manual Refresh (optional for pull-to-refresh / retry)
    fun refresh() {
        fetchVideos()
    }
}