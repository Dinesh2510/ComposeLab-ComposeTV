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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.pixeldev.composetv.presentation.screens.settings.SettingsScreen
import com.pixeldev.composetv.presentation.screens.webview.WebViewScreen
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(navController1: NavHostController) {
    ProModalDrawerScreen(navController1)
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ProModalDrawerScreen(parentNavController1: NavHostController) {
    val navController = rememberNavController()
    val drawerState   = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope         = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        Screen.Search,
        Screen.Home,
        Screen.Favorite,
        Screen.PrivacyPolicy,
        Screen.Terms
    )

    // ── FIX: one FocusRequester per menu item + settings ──
    val menuFocusRequesters = remember {
        List(menuItems.size) { FocusRequester() }
    }
    val settingsFocusRequester = remember { FocusRequester() }

    // ── FIX: when drawer opens → request focus on active item ──
    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.currentValue == DrawerValue.Open) {
            delay(200) // wait for drawer animation to finish
            try {
                when (currentRoute) {
                    Screen.Settings.route -> settingsFocusRequester.requestFocus()
                    else -> {
                        val activeIndex = menuItems.indexOfFirst {
                            it.route == currentRoute
                        }
                        if (activeIndex >= 0) {
                            menuFocusRequesters[activeIndex].requestFocus()
                        }
                    }
                }
            } catch (_: Exception) {}
        }
    }

    // ── FIX: also re-focus when route changes while drawer is open ──
    LaunchedEffect(currentRoute) {
        if (drawerState.currentValue == DrawerValue.Open) {
            delay(100)
            try {
                val activeIndex = menuItems.indexOfFirst { it.route == currentRoute }
                if (activeIndex >= 0) {
                    menuFocusRequesters[activeIndex].requestFocus()
                } else if (currentRoute == Screen.Settings.route) {
                    settingsFocusRequester.requestFocus()
                }
            } catch (_: Exception) {}
        }
    }

    val drawerGradient = Brush.horizontalGradient(
        0.0f  to Color(0xFF020B16),
        0.25f to Color(0xFF020B16),
        0.55f to Color(0xFF020B16).copy(alpha = 0.85f),
        0.75f to Color(0xFF020B16).copy(alpha = 0.6f),
        0.9f  to Color(0xFF020B16).copy(alpha = 0.3f),
        1.0f  to Color.Transparent
    )

    val scrimBrush = Brush.horizontalGradient(
        0.0f  to Color(0xFF020B16).copy(alpha = 0.95f),
        0.4f  to Color(0xFF020B16).copy(alpha = 0.6f),
        0.7f  to Color(0xFF020B16).copy(alpha = 0.3f),
        1.0f  to Color.Transparent
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimBrush  = scrimBrush,
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

                // ── HEADER ───────────────────────────────
                NavigationDrawerItem(
                    selected = false,
                    onClick  = {},
                    leadingContent = {
                        Icon(Icons.Default.AccountCircle, null, Modifier.size(32.dp))
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        focusedContainerColor  = Color.White.copy(alpha = 0.1f),
                        selectedContainerColor = Color.Transparent,
                        focusedContentColor    = Color.White,
                        selectedContentColor   = Color.White
                    )
                ) {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // ── MENU ITEMS ───────────────────────────
                menuItems.forEachIndexed { index, screen ->

                    val isActive = currentRoute == screen.route
                    var isFocused by remember { mutableStateOf(false) }

                    NavigationDrawerItem(
                        selected = isActive,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState    = false
                            }
                            scope.launch { drawerState.setValue(DrawerValue.Closed) }
                        },
                        // ── FIX: attach focus requester ──
                        modifier = Modifier.focusRequester(
                            menuFocusRequesters[index]
                        ).onFocusChanged { isFocused = it.isFocused }
                        ,
                        leadingContent = {
                            screen.icon?.let {
                                Icon(
                                    it,
                                    contentDescription = null,
                                    // ── FIX: orange tint when active ──
                                    tint = when {
                                        isFocused -> Color.Gray
                                        isActive  -> Color(0xFFFF6B1A)
                                        else      -> Color.Gray
                                    }
                                )
                            }
                        },
                        // ── FIX: proper colors ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
                       /* colors = NavigationDrawerItemDefaults.colors(
                            containerColor         = Color.Transparent,
                            focusedContainerColor  = Color(0xFFFF6B1A),
                            selectedContainerColor = Color.White.copy(alpha = 0.12f),
                            focusedSelectedContainerColor = Color(0xFFFF6B1A),
                            contentColor           = Color.White.copy(alpha = 0.55f),
                            focusedContentColor    = Color.White,
                            selectedContentColor   = Color.White,
                            focusedSelectedContentColor = Color.White
                        )*/
                    ) {
                        screen.label?.let {
                            Text(
                                it,
                                fontWeight = if (isActive) FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // ── SETTINGS ─────────────────────────────
                val isSettingsActive = currentRoute == Screen.Settings.route

                NavigationDrawerItem(
                    selected = isSettingsActive,
                    onClick  = {
                        navController.navigate(Screen.Settings.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState    = false
                        }
                        scope.launch { drawerState.setValue(DrawerValue.Closed) }
                    },
                    // ── FIX: attach settings focus requester ──
                    modifier = Modifier.focusRequester(settingsFocusRequester),
                    leadingContent = {
                        Screen.Settings.icon?.let {
                            Icon(
                                it, null,
                                tint = if (isSettingsActive) Color(0xFFFF6B1A)
                                else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        containerColor         = Color.Transparent,
                        focusedContainerColor  = Color(0xFFFF6B1A),
                        selectedContainerColor = Color.White.copy(alpha = 0.12f),
                        focusedSelectedContainerColor = Color(0xFFFF6B1A),
                        contentColor           = Color.White.copy(alpha = 0.55f),
                        focusedContentColor    = Color.White,
                        selectedContentColor   = Color.White,
                        focusedSelectedContentColor = Color.White
                    )
                ) {
                    Screen.Settings.label?.let {
                        Text(
                            it,
                            fontWeight = if (isSettingsActive) FontWeight.SemiBold
                            else FontWeight.Normal
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 80.dp)
        ) {
            NavHost(
                navController    = navController,
                startDestination = Screen.Home.route,
                modifier         = Modifier.fillMaxSize()
            ) {
                composable(Screen.Search.route) {
                    SearchScreen(parentNavController1) {}
                }
                composable(Screen.Home.route) {
                    HomeScreen(parentNavController1)
                }
                composable(Screen.Favorite.route) {
                    WishlistScreen(parentNavController1) {
                        navController.navigate(Screen.Home.route)
                    }
                }
                composable(Screen.PrivacyPolicy.route) {
                    WebViewScreen(url = "https://www.android.com/", "Privacy Policy") {}
                }
                composable(Screen.Terms.route) {
                    WebViewScreen(url = "https://www.apple.com/in/", "Terms & Condition") {}
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}
/*
@Composable
fun SettingsScreen() {
    ScreenUI("Settings")
}
*/


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