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
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import com.pixeldev.composetv.ui.theme.PrimeBackground
import com.pixeldev.composetv.ui.theme.PrimeSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {

    val viewModel: SplashViewModel = hiltViewModel()
    val onboardingDone by viewModel.onboardingDone.collectAsState(initial = null)
    val syncState by viewModel.syncState.collectAsState(initial = ResultState.Loading)

    // 🎨 Colors
    val PrimeBackground = Color(0xFF00050D)
    val PrimeSurface = Color(0xFF1A242F)

    // 🔥 Animations
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Wait until onboarding is loaded
    if (onboardingDone == null) return

    // 🔥 Start animation once
    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(900, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(900)
            )
        }
    }

    // 🔥 Navigation logic (UNCHANGED)
    LaunchedEffect(syncState, onboardingDone) {
        if (syncState is ResultState.Success) {
            delay(500)
            val route =
                if (onboardingDone == true) Screen.Dashboard.route
                else Screen.Onboarding.route

            navController.navigate(route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // 🔥 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00050D), // deep black
                        Color(0xFF020617), // darker (less gray)
                        Color(0xFF050B18), // smooth blend
                        Color(0xFF0A1624)  // subtle lift (NOT too bright)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value * glow
                scaleY = scale.value * glow
                this.alpha = alpha.value
            }
        ) {

            // 🔥 Glow behind logo (IMPORTANT FIX)
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x332196F3), // blue glow
                                Color(0x332A0F6F), // purple glow
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_banner),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .width(260.dp)
                        .height(140.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 Gradient TEXT (matches logo)
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFB388FF), // purple
                            Color(0xFF2196F3)  // blue
                        )
                    )
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.app_name_subtitle),
                color = Color(0xFFB0BEC5), // soft gray (not white)
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 LOADER / STATE
            when (syncState) {

                is ResultState.Loading -> {
                   // HotstarLoader()
                }

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
                  //  HotstarLoader() // keep until navigation
                }
            }
        }
    }
}