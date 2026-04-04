package com.pixeldev.composetv.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.pixeldev.composetv.R
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.presentation.components.HotstarLoader
import com.pixeldev.composetv.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val viewModel: SplashViewModel = hiltViewModel()
    val onboardingDone by viewModel.onboardingDone.collectAsState(initial = null)
    val syncState by viewModel.syncState.collectAsState(initial = ResultState.Loading)

    // Wait until onboarding is loaded
    if (onboardingDone == null) return

    LaunchedEffect(syncState, onboardingDone) {
        if (syncState is ResultState.Success) {
            delay(500)
            val route = if (onboardingDone == true) Screen.Home.route else Screen.Onboarding.route
            navController.navigate(route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.app_banner),
                contentDescription = "Description for accessibility", // Use null if decorative
                modifier = Modifier
                    .width(312.dp)
                    .height(180.dp)
            )
            Text(
                text = "JET STREAM",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (syncState) {
                is ResultState.Loading -> HotstarLoader() // your cute loader below title

                is ResultState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Failed to load data", color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }

                is ResultState.Success -> {
                    HotstarLoader() // keep loader visible until navigation
                }
            }
        }
    }
}