package com.pixeldev.composetv.presentation.screens.search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.DataStoreManager
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.data.repository.VideoRepository
import com.pixeldev.composetv.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    private val _results = MutableStateFlow<List<VideoEntity>>(emptyList())
    val results: StateFlow<List<VideoEntity>> = _results

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    init {
        observeSuggestions()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    fun onSearch() {
        if (query.isBlank()) return

        viewModelScope.launch {
            repository.searchVideos(query).collect {
                _results.value = it
                _hasSearched.value = true
            }
        }
    }

    private fun observeSuggestions() {
        viewModelScope.launch {
            snapshotFlow { query }
                .debounce(200)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { repository.getSearchSuggestions(it) }
                .collect {
                    _suggestions.value = it
                }
        }
    }

    val randomTitles: StateFlow<List<String>> =
        repository.getRandomTitles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}
/*
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    private val _results = MutableStateFlow<List<VideoEntity>>(emptyList())
    val results: StateFlow<List<VideoEntity>> = _results

    init {
        observeSearch()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            snapshotFlow { query }
                .debounce(300)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { repository.searchVideos(it) }
                .collect {
                    _results.value = it
                }
        }
    }
}*/