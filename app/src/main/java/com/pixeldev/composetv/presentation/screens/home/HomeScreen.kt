package com.pixeldev.composetv.presentation.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.components.ExitDialog
import com.pixeldev.composetv.presentation.components.VideoCard
import com.pixeldev.composetv.presentation.components.VideoCardStd
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

@Composable
fun HomeScreen(
    viewModel: VideoViewModel = hiltViewModel()
) {

    val videos by viewModel.videos.collectAsState()
    val state by viewModel.syncState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    /* Column() {

         WishlistScreen()
         Text("=======================================")
         Button({ showExitDialog = true}) {Text("asljhgf") }
         VideoList(videos, viewModel)
     }*/
    val groupedVideos = videos.groupBy { it.category }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { clip = false }, // 🔥 instead of clipToBounds
        contentPadding = PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedVideos.forEach { (category, videoList) ->
            item {
                CategoryRow(
                    title = category,
                    videos = videoList,
                    viewModel = viewModel
                )
            }
        }
    }

}
@Composable
fun CategoryRow(
    title: String,
    videos: List<VideoEntity>,
    viewModel: VideoViewModel
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1f,
        label = "rowScale"
    )

    val extraPadding by animateDpAsState(
        targetValue = if (isFocused) 16.dp else 0.dp,
        label = "rowPadding"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = extraPadding) // ✅ THIS FIXES OVERLAP
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0.5f) // 🔥 LEFT ANCHOR
                clip = false // 🔥 critical

            }
            .onFocusChanged {
                isFocused = it.hasFocus
            }
    ) {

        Text(
            text = title,
            color = if (isFocused) Color.White else Color.LightGray,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 32.dp, ) // match row padding
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.graphicsLayer { clip = false },
            contentPadding = PaddingValues(
                start = 32.dp,  // 🔥 increase this
                end = 64.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(videos.size) { video ->
                VideoCardStd(videos[video], viewModel)
            }
        }
    }
}
/*
*
    BackHandler(enabled = showExitDialog) {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitDialog(
            message = "Are you sure you want to exit?",
            primaryButtonText = "Exit",
            onPrimaryClick = {
                // Handle exit
                showExitDialog = false
                // call activity.finish() or navController.popBackStack() etc.
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }

* */