package com.pixeldev.composetv.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeDetailsViewModel @Inject constructor(
    private val repository: VideoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val videoTitle: String? = savedStateHandle["vidID"]

    @OptIn(ExperimentalCoroutinesApi::class)
    val videoDetails: StateFlow<VideoEntity?> = flow {
        videoTitle?.let { title ->
            emitAll(repository.getVideoByTitle(title))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val relatedVideos: StateFlow<List<VideoEntity>> = videoDetails
        .filterNotNull()
        .flatMapLatest { video ->
            repository.getVideosByCategory(video.category)
                .map { list ->
                    list.filter { it.title != video.title }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ❤️ Toggle Wishlist — Room update triggers videoDetails recompose instantly
    fun toggleWishlist() {
        viewModelScope.launch {
            val current = videoDetails.value ?: return@launch
            repository.toggleWishlist(
                title = current.title,
                isWish = !current.isWishlist
            )
            // No manual state needed — Room Flow emits updated entity automatically
        }
    }
}