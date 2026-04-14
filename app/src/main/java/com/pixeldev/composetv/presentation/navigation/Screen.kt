package com.pixeldev.composetv.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String? = null, val icon: ImageVector? = null) {
    // Auth/Startup Flow
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    // Main Wrapper
    object Dashboard : Screen("dashboard")

    object HomeDetails : Screen("homedetails")


    // Drawer Items
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Favorite : Screen("wishlist", "Wishlist", Icons.Default.Bookmark)
    object Shows : Screen("shows", "Shows", Icons.Default.Tv)
    object Library : Screen("library", "Library", Icons.Default.VideoLibrary)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}