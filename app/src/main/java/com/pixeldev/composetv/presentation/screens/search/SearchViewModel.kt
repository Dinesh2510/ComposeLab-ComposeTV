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
import kotlinx.coroutines.Job
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

    private var searchJob: Job? = null // 🔥 prevent multiple collectors

    init {
        observeSuggestions()
    }

    // ✅ HANDLE QUERY CHANGE (RESET STATE PROPERLY)
    fun onQueryChange(newQuery: String) {
        query = newQuery

        if (newQuery.isBlank()) {
            // 🔥 FULL RESET (important for TV UX)
            searchJob?.cancel()
            _results.value = emptyList()
            _suggestions.value = emptyList()
            _hasSearched.value = false
        } else {
            // typing again → reset search state but keep suggestions
            _hasSearched.value = false
            _results.value = emptyList()
        }
    }

    // ✅ SEARCH (SINGLE SOURCE OF TRUTH)
    fun onSearch() {
        if (query.isBlank()) return

        // cancel previous search
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            repository.searchVideos(query).collect { result ->
                _results.value = result
                _hasSearched.value = true
                _suggestions.value = emptyList() // 🔥 hide suggestions after search
            }
        }
    }

    // ✅ SUGGESTIONS FLOW (SAFE + CLEAN)
    private fun observeSuggestions() {
        viewModelScope.launch {
            snapshotFlow { query }
                .debounce(250)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { repository.getSearchSuggestions(it) }
                .collect { suggestions ->
                    _suggestions.value = suggestions
                }
        }
    }

    // ✅ TRENDING
    val randomTitles: StateFlow<List<String>> =
        repository.getRandomTitles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}