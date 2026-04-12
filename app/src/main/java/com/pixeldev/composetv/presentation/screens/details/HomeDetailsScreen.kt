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

// Image (Coil)
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

// TV Lazy Components


// DP Units
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import kotlinx.coroutines.launch


@Composable
fun HomeDetailsScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // 🔥 BACKGROUND IMAGE
        AsyncImage(
            model = "https://storage.googleapis.com/androiddevelopers/samples_assets/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202011_%20Year%20In%20Review/bg.jpg",
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
            item { MovieHeaderSection() }

            item { ActionButtons() }

            item { MovieDescription() }

            item { RelatedSection() } // 👈 important
        }
    }
}
@Composable
fun MovieHeaderSection() {
    Column {

        Text(
            text = "SPIDER-MAN",
            color = Color.Red,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "No Way Home",
            color = Color.White,
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "#7 in action movies",
            color = Color.Green,
            fontSize = 14.sp
        )
    }
}
@Composable
fun ActionButtons() {

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        TvButton(
            text = "Watch again",
            isPrimary = false
        )

        TvButton(
            text = "More ways to watch",
            isPrimary = false
        )
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
fun MovieDescription() {
    Column {

        Text(
            text = "Spider-Man faces several unwanted guests when a spell by Doctor Strange backfires.",
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
fun RelatedSection() {

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
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
            text = "Related",
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                RelatedItem()
            }
        }
    }
}

@Composable
fun RelatedItem() {

    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isFocused) 1.1f else 1f)

    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 260.dp)
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    ) {

        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/sample.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
        )

        if (isFocused) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            )
        }
    }
}