package com.pixeldev.composetv.presentation.screens.wishlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Text
import com.pixeldev.composetv.presentation.screens.home.VideoViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.domain.model.Video
import com.pixeldev.composetv.presentation.navigation.Screen
import com.pixeldev.composetv.presentation.screens.details.LoadingScreen

@Composable
fun WishlistScreen(
    navController: NavController,
    viewModel: VideoViewModel = hiltViewModel(),
    onGoHome: () -> Unit // <-- pass navigation lambda from NavHost
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        ModernImmersiveGridScreen(
            viewModel = viewModel,
            onGoHome = onGoHome,
          navController =  navController
        )
    }
}





@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ModernImmersiveGridScreen(
    viewModel: VideoViewModel,
    onGoHome: () -> Unit,
    navController: NavController
    ) {
   // val wishlist by viewModel.wishlist.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val wishlist by viewModel.wishlistFlow
        .collectAsStateWithLifecycle(initialValue = emptyList())
// Don't flash empty screen while data is loading
    if (syncState is ResultState.Loading && wishlist.isEmpty()) {
        // Show shimmer/loading indicator instead
        LoadingScreen()
        return
    }

    if (wishlist.isEmpty()) {
        EmptyWishlistScreen(onGoHome = onGoHome)
        return
    }
    // Safely handle empty wishlist
    val initialMovie = wishlist.firstOrNull()

    var currentFocusedMovie by remember { mutableStateOf<VideoEntity?>(initialMovie) }

    val firstItemFocusRequester = remember { FocusRequester() }
    var hasRequestedInitialFocus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D12))
    ) {

        // --- BACKGROUND ---
        Crossfade(
            targetState = currentFocusedMovie,
            animationSpec = tween(600),
            label = "bg_fade"
        ) { movie ->
            movie?.let {
                AsyncImage(
                    model = it.card,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Gradient overlays
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        0.0f to Color(0xFF0D0D12).copy(alpha = 0.95f),
                        0.6f to Color(0xFF0D0D12).copy(alpha = 0.5f),
                        1.0f to Color.Transparent
                    )
                )
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        1.0f to Color(0xFF0D0D12).copy(alpha = 0.9f)
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Banner
            currentFocusedMovie?.let { movie ->
                MovieMetadataBanner(
                    movie = movie,
                    modifier = Modifier.padding(58.dp, 64.dp, 58.dp, 32.dp)
                )
            }

            // GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 58.dp,
                    top = 58.dp,
                    end = 58.dp,
                    bottom = 48.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                itemsIndexed(
                    items = wishlist,
                    key = { _, movie -> movie.title } // 👈 MUST be unique id
                ) { index, movie ->

                    val focusModifier =
                        if (index == 0) {
                            Modifier
                                .focusRequester(firstItemFocusRequester)
                                .onGloballyPositioned {
                                    if (!hasRequestedInitialFocus && wishlist.isNotEmpty()) {
                                        firstItemFocusRequester.requestFocus()
                                        hasRequestedInitialFocus = true
                                    }
                                }
                        } else Modifier

                    MovieCard(
                        movie = movie,
                        onFocused = { currentFocusedMovie = it },
                        modifier = focusModifier,
                        onClickCard = { navController.navigate(Screen.HomeDetails.route+ "/${movie.title}")}
                    )
                }
            }
        }
    }
}
// 3. The Movie Card Composable
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(movie: VideoEntity, onFocused: (VideoEntity) -> Unit, modifier: Modifier = Modifier, onClickCard:(VideoEntity)-> Unit) {
    Card(
        onClick = { onClickCard(movie) },
        colors = CardDefaults.colors(
            containerColor = Color.DarkGray,
            focusedContainerColor = Color.DarkGray
        ),
        scale = CardDefaults.scale(focusedScale = 1.1f),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(3.dp, Color.White),
                shape = RoundedCornerShape(12.dp)
            )
        ),
        shape = CardDefaults.shape(shape = RoundedCornerShape(12.dp)),
        modifier = modifier
            .width(178.dp)
            .height(100.dp)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocused(movie)
                }
            }
    ) {
        AsyncImage(
            model = movie.card,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// 4. The Movie Banner Composable
@Composable
fun MovieMetadataBanner(movie: VideoEntity, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(0.5f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = movie.category, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
           /* Text(
                text = "${movie.genre}  •  ${movie.year}  •  ${movie.duration}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )*/
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = movie.description,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EmptyWishlistScreen(
    onGoHome: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D12)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Your wishlist is empty",
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Find movies you love and add them here ❤️",
                color = Color.LightGray,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onGoHome,
                modifier = Modifier
                    .focusRequester(focusRequester)
            ) {
                Text("Browse Videos")
            }
        }
    }
}

