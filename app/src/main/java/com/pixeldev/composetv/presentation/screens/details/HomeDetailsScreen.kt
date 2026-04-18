package com.pixeldev.composetv.presentation.screens.details

/*
 * Project: ComposeTv Demo
 * Package: com.pixeldev.composetv.presentation.screens.details
 *
 * Copyright 2026 Dinesh
 * GitHub: https://github.com/Dinesh2510
 *
 * Created on: Saturday, April 11, 2026 at 20:08
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Compose Core
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// Layout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border

// Shapes & Graphics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Focus (VERY IMPORTANT for TV)
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.focusable

// Animation
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.Alignment

// Image (Coil)
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

// TV Lazy Components


// DP Units
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.Text
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.components.HotstarLoader
import com.pixeldev.composetv.presentation.components.VideoCardStd
import com.pixeldev.composetv.presentation.components.VideoCardStdFocus
import kotlinx.coroutines.launch

@Composable
fun HomeDetailsScreen(
    videoTitle: String,
    onBackPressed: () -> Unit,
    viewModel: HomeDetailsViewModel = hiltViewModel()
) {
    val videoDataItem by viewModel.videoDetails.collectAsStateWithLifecycle()
    val relatedItems by viewModel.relatedVideos.collectAsStateWithLifecycle()

    Log.d("Details", "title: $videoTitle")
    Log.d("Details", "video: $videoDataItem")
    Log.d("Details", "related: $relatedItems")

    // ✅ Single source of truth for UI state
    val videoData = videoDataItem ?: return LoadingScreen()
    /*    val activity = LocalContext.current as ComponentActivity
        val sharedViewModel: SharedViewModel = hiltViewModel(activity)
        val videoData by sharedViewModel.selectedVideo.collectAsState()
        Log.e("TAG_VM", "HomeDetailsScreen: $videoData")*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // 🔥 BACKGROUND IMAGE
        AsyncImage(
            model = videoData!!.card,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔥 DARK GRADIENT OVERLAY (IMPORTANT for readability)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.95f),
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    )
                )
        )

        // 🔥 CONTENT
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 60.dp, top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item { MovieHeaderSection(videoData) }

            item { ActionButtons(
                isWishlisted = videoData.isWishlist,
                onWishlistToggle = { viewModel.toggleWishlist() }
            ) }


            item { MovieDescription(videoData) }

            item { RelatedSection(relatedItems) } // 👈 important
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        HotstarLoader()
    }
}

@Composable
fun MovieHeaderSection(videoData: VideoEntity?) {
    videoData?.let { video ->
        Column {

            Text(
                text = videoData.title,
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = videoData.category,
                color = Color.White,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "#7 in action movies",
                color = Color.Green,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ActionButtons(
    isWishlisted: Boolean,
    onWishlistToggle: () -> Unit
) {

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        TvButton(
            text = "Watch again",
            isPrimary = false
        )

        TvButton(
            text = "More ways to watch",
            isPrimary = false
        )
        // Example — adapt to your existing layout
        IconButton(onClick = onWishlistToggle) {
            Icon(
                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isWishlisted) "Remove from Wishlist" else "Add to Wishlist",
                tint = if (isWishlisted) Color.Red else Color.Gray
            )
        }
    }
}

@Composable
fun TvButton(
    text: String,
    isPrimary: Boolean
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isFocused -> Color.White               // 🔥 FOCUS = ALWAYS WHITE
            isPrimary -> Color.White.copy(alpha = 0.85f)
            else -> Color.DarkGray
        }
    )

    val textColor by animateColorAsState(
        targetValue = if (isFocused || isPrimary) Color.Black else Color.White
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isFocused) 4.dp else 0.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun MovieDescription(videoData: VideoEntity?) {
    Column {

        Text(
            text = videoData?.description ?: "",
            color = Color.White,
            fontSize = 16.sp,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Action • Adventure • IMDb 8.1 • 2021 • 2h 22m",
            color = Color.LightGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Cast: Tom Holland, Zendaya, Benedict Cumberbatch",
            color = Color.LightGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RelatedSection(relatedItems: List<VideoEntity>) {

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = 22.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged { state ->
                if (state.isFocused) {
                    scope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }
    ) {

        Text(
            text = "Related Videos",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 12.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // please parse rhe list relatedItems into items key couint contenttype
            // TODO: Onclick Remaining
            items(count = relatedItems.size) { index->
                VideoCardStdFocus(relatedItems[index])
            }
        }
    }
}

