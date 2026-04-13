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

import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api

@Composable
fun WishlistScreen( viewModel: VideoViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        ModernImmersiveGridScreen()
    }
    /*val wishlist by viewModel.wishlist.collectAsState()

    LazyColumn {
        items(wishlist.size) {
            Text(wishlist[it].title)
        }
    }*/
}



// 1. Data Model
data class Movie(
    val id: String,
    val title: String,
    val description: String,
    val rating: String,
    val genre: String,
    val year: String,
    val duration: String,
    val imageUrl: String
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ModernImmersiveGridScreen() {

    val dummyMovies = generateOnlineDummyMovies()

    var currentFocusedMovie by remember { mutableStateOf(dummyMovies.first()) }

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
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
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

        // --- FOREGROUND ---
        Column(modifier = Modifier.fillMaxSize()) {

            // Banner
            MovieMetadataBanner(
                movie = currentFocusedMovie,
                modifier = Modifier.padding(58.dp, 64.dp, 58.dp, 32.dp)
            )

            // GRID SECTION
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 👈 change to 3 or 4
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

                itemsIndexed(dummyMovies) { index, movie ->

                    val focusModifier =
                        if (index == 0) {
                            Modifier
                                .focusRequester(firstItemFocusRequester)
                                .onGloballyPositioned {
                                    if (!hasRequestedInitialFocus) {
                                        firstItemFocusRequester.requestFocus()
                                        hasRequestedInitialFocus = true
                                    }
                                }
                        } else Modifier

                    MovieCard(
                        movie = movie,
                        onFocused = { currentFocusedMovie = it },
                        modifier = focusModifier
                    )
                }
            }
        }
    }
}
// 3. The Movie Card Composable
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(movie: Movie, onFocused: (Movie) -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = { /* Handle Click Events */ },
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
            model = movie.imageUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// 4. The Movie Banner Composable
@Composable
fun MovieMetadataBanner(movie: Movie, modifier: Modifier = Modifier) {
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
                Text(text = movie.rating, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            Text(
                text = "${movie.genre}  •  ${movie.year}  •  ${movie.duration}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
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

// 5. Dummy Data Generator
fun generateOnlineDummyMovies(): List<Movie> {
    return listOf(
        Movie("1", "Power Sisters", "A dynamic duo of superhero siblings join forces to save their city from a sinister villain.", "U/A 13+", "Action", "2022", "2h 15m",
            "https://images.unsplash.com/photo-1626814026160-2237a95fc5a0?q=80&w=2070&auto=format&fit=crop"),
        Movie("2", "Neon Cyberpunk", "A detective hunts down a rogue AI in the rain-soaked streets of a futuristic metropolis.", "A", "Sci-Fi", "2024", "1h 50m",
            "https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=2025&auto=format&fit=crop"),
        Movie("3", "The Martian Horizon", "A crew struggles to survive on the red planet after a catastrophic habitat failure.", "U", "Sci-Fi/Drama", "2023", "2h 30m",
            "https://images.unsplash.com/photo-1614730321146-b6fa6a46bcb4?q=80&w=1974&auto=format&fit=crop"),
        Movie("4", "Deep Ocean", "Researchers discover something ancient and terrifying sleeping at the bottom of the Mariana Trench.", "U/A 16+", "Thriller", "2021", "1h 45m",
            "https://images.unsplash.com/photo-1582967788606-a171c1080cb0?q=80&w=1964&auto=format&fit=crop"),
        Movie("5", "Mountain Edge", "A legendary climber attempts the impossible: summiting the deadliest peak in winter.", "U", "Documentary", "2022", "1h 25m",
            "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=2070&auto=format&fit=crop")
    )
}

