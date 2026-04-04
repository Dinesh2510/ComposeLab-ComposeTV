package com.pixeldev.composetv.presentation.screens.onboard
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixeldev.composetv.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            dataStoreManager.setOnboardingDone()
        }
    }
}