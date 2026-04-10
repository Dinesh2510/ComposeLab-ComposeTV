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
import com.pixeldev.composetv.presentation.components.ExitDialogOverlay
import com.pixeldev.composetv.presentation.components.VideoCardStd
import com.pixeldev.composetv.presentation.components.VideoCardStdFocus
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

import coil.compose.AsyncImage
import androidx.tv.material3.*
import kotlinx.coroutines.delay

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.pixeldev.composetv.presentation.components.VideoCard

@Composable
fun HomeScreen(
   // viewModel: VideoViewModel = hiltViewModel()
) {
    ModernImmersiveListScreen()
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ModernImmersiveListScreen(
    viewModel: VideoViewModel = hiltViewModel()
) {
    val videos by viewModel.videos.collectAsState()

    val groupedVideos = remember(videos) {
        videos.groupBy { it.category }
    }

    var currentFocusedVideo by remember {
        mutableStateOf<VideoEntity?>(null)
    }

    val firstItemFocusRequester = remember { FocusRequester() }
    var hasRequestedInitialFocus by remember { mutableStateOf(false) }

    // Set first video when data loads
    LaunchedEffect(videos) {
        if (videos.isNotEmpty() && currentFocusedVideo == null) {
            currentFocusedVideo = videos.first()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D12))
    ) {

        // 🔥 BACKGROUND
        currentFocusedVideo?.let { video ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {

                Crossfade(
                    targetState = video,
                    animationSpec = tween(600),
                    label = "bg_fade",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                ) { item ->
                    AsyncImage(
                        model = item.background,
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Horizontal Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .background(
                            Brush.horizontalGradient(
                                0f to Color(0xFF0D0D12).copy(alpha = 0.95f),
                                0.6f to Color(0xFF0D0D12).copy(alpha = 0.5f),
                                1f to Color.Transparent
                            )
                        )
                )

                // Vertical Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                1f to Color(0xFF0D0D12)
                            )
                        )
                )
            }
        }

        // 🔥 FOREGROUND CONTENT
        Column(modifier = Modifier.fillMaxSize()) {

            // 🔹 Banner
            currentFocusedVideo?.let {
                VideoMetadataBanner(
                    video = it,
                    modifier = Modifier.padding(
                        start = 58.dp,
                        top = 74.dp,
                        end = 58.dp,
                        bottom = 74.dp
                    )
                )
            }

            // 🔹 Categories List
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 48.dp)
            ) {

                groupedVideos.entries.forEachIndexed { rowIndex, entry ->

                    val category = entry.key
                    val videoList = entry.value

                    item {
                        Text(
                            text = category,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(
                                start = 58.dp,
                                bottom = 14.dp,
                                top = if (rowIndex == 0) 0.dp else 24.dp
                            )
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(start = 58.dp, end = 58.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            itemsIndexed(videoList) { index, video ->

                                val focusModifier =
                                    if (rowIndex == 0 && index == 0) {
                                        Modifier
                                            .focusRequester(firstItemFocusRequester)
                                            .onGloballyPositioned {
                                                if (!hasRequestedInitialFocus) {
                                                    firstItemFocusRequester.requestFocus()
                                                    hasRequestedInitialFocus = true
                                                }
                                            }
                                    } else Modifier

                                VideoCard(
                                    video = video,
                                    onFocused = { currentFocusedVideo = it },
                                    modifier = focusModifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun VideoMetadataBanner(
    video: VideoEntity,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(0.5f)) {

        // Optional meta row (only if fields exist in your model)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            video.studio?.let {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = it,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            val metaText = listOfNotNull(
                "Entertainment",
                "2026",
                "2h 30m"
            ).joinToString("  •  ")

            if (metaText.isNotEmpty()) {
                Text(
                    text = metaText,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = video.title,
            color = Color.White,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = video.description ?: "",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}