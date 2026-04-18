package com.pixeldev.composetv.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.pixeldev.composetv.presentation.screens.home.HomeScreen
import kotlinx.coroutines.launch

import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.tv.material3.*

import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.pixeldev.composetv.presentation.components.VideoCard
import com.pixeldev.composetv.presentation.navigation.Screen

import androidx.compose.material.icons.filled.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.components.TvAppBackground
import com.pixeldev.composetv.presentation.components.TvAppBackgroundNew
import com.pixeldev.composetv.presentation.components.TvAppBackgroundNewtt
import com.pixeldev.composetv.presentation.screens.search.SearchScreen
import com.pixeldev.composetv.presentation.screens.search.TvSearchBar
import com.pixeldev.composetv.presentation.screens.webview.WebViewScreen
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

@Composable
fun DashboardScreen(navController1: NavHostController) {
    ProModalDrawerScreen(navController1)
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ProModalDrawerScreen(parentNavController1: NavHostController) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Track current route to highlight the correct drawer item
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        Screen.Search,
        Screen.Home,
        Screen.Favorite,
        Screen.PrivacyPolicy,
        Screen.Terms
    )

    val drawerGradient = Brush.horizontalGradient(
        0.0f to Color(0xFF020B16),              // deep blue-black (NOT pure black)
        0.25f to Color(0xFF020B16),             // keep left solid
        0.55f to Color(0xFF020B16).copy(alpha = 0.85f),
        0.75f to Color(0xFF020B16).copy(alpha = 0.6f),
        0.9f to Color(0xFF020B16).copy(alpha = 0.3f),
        1.0f to Color.Transparent
    )
    val scrimBrush = Brush.horizontalGradient(
        0.0f to Color(0xFF020B16).copy(alpha = 0.95f),
        0.4f to Color(0xFF020B16).copy(alpha = 0.6f),
        0.7f to Color(0xFF020B16).copy(alpha = 0.3f),
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
                composable(Screen.Search.route) { SearchScreen(parentNavController1){} }
                composable(Screen.Home.route) { HomeScreen(parentNavController1) }
                composable(Screen.Favorite.route) { WishlistScreen(parentNavController1){
                    navController.navigate(Screen.Home.route)
                } }
                composable(Screen.PrivacyPolicy.route) { WebViewScreen(url = "https://www.android.com/", "Privacy Policy"){} }
                composable(Screen.Terms.route) { WebViewScreen(url = "https://www.apple.com/in/", "Terms & Condition"){} }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    ScreenUI("Settings")
}


@Composable
fun ScreenUI(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TvAppBackgroundNewtt( glowColor = Color(0xFFE53935) )
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
            onFocused = {},
            onClickCard = {}
        )
    }
}