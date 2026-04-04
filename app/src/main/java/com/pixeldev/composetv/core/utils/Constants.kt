package com.pixeldev.composetv.core.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

class Constants {

    companion object {
        //https://storage.googleapis.com/androiddevelopers/samples_assets/android-tv/android_tv_videos_new.json
        const val BASE_URL = "storage.googleapis.com"

        val AppGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27FF), // Purple
                Color(0xFF2196F3)  // Blue
            ),
            start = Offset(0f, 0f), // top-left
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // bottom-right
        )
        val StreamposeBackground = Brush.linearGradient(
            colors = listOf(
                Color(0xFF0A0F1F), // Dark Navy
                Color(0xFF000000)  // Black
            ),
            start = Offset(0f, 0f), // top-left
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // bottom-right
        )
    }

}