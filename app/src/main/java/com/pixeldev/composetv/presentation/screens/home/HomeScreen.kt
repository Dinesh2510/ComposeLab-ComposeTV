package com.pixeldev.composetv.presentation.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
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
fun HomeScreen(    viewModel: VideoViewModel = hiltViewModel()
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

    LazyColumn {

        groupedVideos.forEach { (category, videoList) ->

            item {
                CategoryRow(
                    title = category,
                    videos = videoList,
                    viewModel = viewModel
                )
            }
        }}

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

}
@Composable
fun CategoryRow(
    title: String,
    videos: List<VideoEntity>,
    viewModel: VideoViewModel
) {

    Column(
        modifier = Modifier.padding( vertical = 12.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(videos.size) { video ->
                VideoCardStd(videos[video], viewModel)
            }
        }
    }
}
