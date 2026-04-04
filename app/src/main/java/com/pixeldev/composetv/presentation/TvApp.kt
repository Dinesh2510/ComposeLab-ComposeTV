package com.pixeldev.composetv.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pixeldev.composetv.presentation.navigation.Screen
import com.pixeldev.composetv.presentation.screens.home.HomeScreen
import com.pixeldev.composetv.presentation.screens.onboard.OnboardingScreen
import com.pixeldev.composetv.presentation.screens.splash.SplashScreen

@Composable
fun TvApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}