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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

@Composable
fun DashboardScreen() {
    ProModalDrawerScreen()
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ProModalDrawerScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Track current route to highlight the correct drawer item
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        Screen.Search,
        Screen.Home,
        Screen.Movies,
        Screen.Shows,
        Screen.Library
    )

    val scrimBrush = Brush.horizontalGradient(
        0.0f to Color(0xFF00050D).copy(alpha = 0.95f), // deep app bg
        0.4f to Color(0xFF00050D).copy(alpha = 0.7f),
        0.7f to Color(0xFF020817).copy(alpha = 0.4f),
        1.0f to Color.Transparent
    )

    val drawerGradient = Brush.horizontalGradient(
        0.0f to Color(0xFF00050D),                // main app bg
        0.5f to Color(0xFF020817),                // slight blue depth
        0.8f to Color(0xFF0A1624).copy(alpha = 0.8f), // matches surface tone
        1.0f to Color.Transparent
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimBrush = scrimBrush,
        drawerContent = { drawerValue ->
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(if (drawerValue == DrawerValue.Open) 280.dp else 80.dp)
                    .background(drawerGradient)
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // --- PROFILE / HEADER ---
                NavigationDrawerItem(
                    selected = false,
                    onClick = { /* Navigate to Profile */ },
                    leadingContent = {
                        Icon(Icons.Default.AccountCircle, null, Modifier.size(32.dp))
                    }
                ) {
                    Column {
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // --- NAVIGATION ITEMS ---
                menuItems.forEach { screen ->
                    NavigationDrawerItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            // Professional Nav Handling: Avoid stack buildup
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.setValue(DrawerValue.Closed) }
                        },
                        leadingContent = {
                            screen.icon?.let {
                                Icon(
                                    it,
                                    contentDescription = null
                                )
                            }
                        },
                        /*colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedContainerColor = Color.White.copy(alpha = 0.25f),
                            selectedContentColor = Color.White,
                            focusedContentColor = Color.White
                        )*/
                    ) {
                        screen.label?.let { Text(it) }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // --- SETTINGS ---
                NavigationDrawerItem(
                    selected = currentRoute == Screen.Settings.route,
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.setValue(DrawerValue.Closed) }
                    },
                    leadingContent = { Screen.Settings.icon?.let { Icon(it, null) } }
                ) {
                    Screen.Settings.label?.let { Text(it) }
                }
            }
        },
    ) {
        // --- NAVIGATION HOST (The Content) ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                // When drawer is closed, rail is 80dp. We add that as padding.
                .padding(start = 80.dp)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Search.route) { SearchScreen() }
                composable(Screen.Home.route) { HomeScreen() }
                composable(Screen.Movies.route) { WishlistScreen() }
                composable(Screen.Shows.route) { ShowsScreen() }
                composable(Screen.Library.route) { LibraryScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}

@Composable
fun ManScreen() {
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

data class NavigationItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun ScreenUI(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        VideoCard(
            video = VideoEntity(
                title = "The Great Adventure",
                description = "A journey into the unknown depths of the forest.",
                videoUrl = "https://example.com/video1.mp4",
                card = "https://example.com/card1.jpg",
                background = "https://example.com/bg1.jpg",
                studio = "Pro Studios",
                category = "Movies"
            ),
            {},
        )
    }
}