package com.pixeldev.composetv.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.pixeldev.composetv.presentation.navigation.Screen
import com.pixeldev.composetv.presentation.screens.dashboard.DashboardScreen
import com.pixeldev.composetv.presentation.screens.details.HomeDetailsScreen
import com.pixeldev.composetv.presentation.screens.home.SharedViewModel
import com.pixeldev.composetv.presentation.screens.onboard.OnboardingScreen
import com.pixeldev.composetv.presentation.screens.splash.SplashScreen

@Composable
fun TvApp() {
    val navController = rememberNavController()
    val videoDetailsTitle = "vidID"

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

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(
            route = Screen.HomeDetails.route + "/{$videoDetailsTitle}",
            arguments = listOf(navArgument(videoDetailsTitle) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(videoDetailsTitle) ?: return@composable

            HomeDetailsScreen(
                videoTitle = movieId,
                onBackPressed = { navController.popBackStack() },
            )
        }
    }
}
