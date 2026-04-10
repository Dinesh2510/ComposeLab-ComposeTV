package com.pixeldev.composetv.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")

    object Home : Screen("home")
    object Search : Screen("search")
    object Movies : Screen("movies")
    object Shows : Screen("shows")
    object Library : Screen("library")
    object Settings : Screen("settings")
}