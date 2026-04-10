package com.pixeldev.composetv.presentation.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Button
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.pixeldev.composetv.presentation.screens.home.HomeScreen
import kotlinx.coroutines.launch

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.clip
import androidx.tv.material3.*

import androidx.compose.ui.focus.onFocusChanged

import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.pixeldev.composetv.presentation.components.VideoCard
import com.pixeldev.composetv.presentation.navigation.Screen
import com.pixeldev.composetv.presentation.screens.home.HomeScreen
import kotlinx.coroutines.launch
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.onPreviewKeyEvent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*

import androidx.compose.material.icons.filled.*
import androidx.compose.ui.res.stringResource
import com.pixeldev.composetv.R
@Composable
fun DashboardScreen() {
    ProModalDrawerScreen()
}



@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ProModalDrawerScreen() {
    val items = listOf(
        NavigationItem("Search", Icons.Default.Search),
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Movies", Icons.Default.Movie),
        NavigationItem("Shows", Icons.Default.Tv),
        NavigationItem("Library", Icons.Default.VideoLibrary)
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(1) }

    val scrimBrush = Brush.horizontalGradient(
        0.0f to Color.Black.copy(alpha = 0.9f),
        0.4f to Color.Black.copy(alpha = 0.5f),
        1.0f to Color.Transparent
    )
    // ModalNavigationDrawer uses DrawerState to manage open/closed flow
    // On TV, it automatically opens when focus moves to the 'rail' area
    ModalNavigationDrawer(
        drawerState = drawerState, // Pass the state here
        scrimBrush = scrimBrush,
        drawerContent = { drawerValue ->
            // This Column represents the actual drawer panel
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(if (drawerValue == DrawerValue.Open) 280.dp else 80.dp)
                    .background(Color(0xFF0F0F0F)) // Deep dark background
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // --- PROFILE SECTION ---
                NavigationDrawerItem(
                    selected = false,
                    onClick = {
                        // Manual Close on click
                        scope.launch { drawerState.setValue(DrawerValue.Closed) }
                    },
                    leadingContent = {
                        Icon(Icons.Default.AccountCircle, null, Modifier.size(32.dp))
                    }
                ) {
                    Column {
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index
                            // --- CLICK TO CLOSE LOGIC ---
                            scope.launch {
                                drawerState.setValue(DrawerValue.Closed)
                            }},
                        leadingContent = { Icon(item.icon, null) },
                        // Custom colors to match the "Pro" gradient/faded look in your screenshot
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedContainerColor = Color.White.copy(alpha = 0.25f),
                            selectedContentColor = Color.White,
                            focusedContentColor = Color.White
                        )
                    ) {
                        Text(item.label)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // --- SETTINGS ---
                NavigationDrawerItem(
                    selected = false,
                    onClick = { },
                    leadingContent = { Icon(Icons.Default.Settings, null) }
                ) {
                    Text("Settings")
                }
            }
        },
    ) {
        // --- MAIN CONTENT (The Grid) ---
        MainContentGrid()
    }
}
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MainContentGrid() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(start = 100.dp, top = 40.dp, end = 20.dp)
    ) {
        Text("Movies for You", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            items(12) { index ->
                Card(
                    onClick = { },
                    modifier = Modifier.aspectRatio(16f/9f),
                    colors = CardDefaults.colors(
                        containerColor = Color(0xFF1E1E1E),
                        focusedContainerColor = Color(0xFF333333)
                    )
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Item $index", color = Color.White)
                    }
                }
            }
        }
    }
}
@Composable
fun HomeScreen() {
    ScreenUI("Home Screen")
}

@Composable
fun SearchScreen() {
    ScreenUI("Search")
}

@Composable
fun MoviesScreen() {
    ScreenUI("Movies")
}

@Composable
fun ShowsScreen() {
    ScreenUI("Shows")
}

@Composable
fun LibraryScreen() {
    ScreenUI("Library")
}

@Composable
fun SettingsScreen() {
    ScreenUI("Settings")
}
data class NavigationItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun ScreenUI(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, color = Color.White)
    }
}