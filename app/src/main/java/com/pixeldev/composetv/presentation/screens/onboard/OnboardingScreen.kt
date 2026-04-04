package com.pixeldev.composetv.presentation.screens.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.R
import com.pixeldev.composetv.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@Composable
fun OnboardingScreen(navController: NavController) {

    val viewModel: OnboardingViewModel = hiltViewModel()

    var page by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    val pages = listOf(
        Triple("Welcome", "Experience the best TV app", R.drawable.app_banner),
        Triple("Unlimited Content", "Watch anytime anywhere", R.drawable.app_banner),
        Triple("Get Started", "Enjoy seamless experience", R.drawable.app_banner)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = pages[page].third),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = pages[page].first,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = pages[page].second,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    if (page < pages.lastIndex) {
                        page++
                    } else {
                        viewModel.completeOnboarding()   // ✅ clean

                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                modifier = Modifier.focusRequester(focusRequester)
            ) {
                Text(
                    text = if (page == pages.lastIndex) "Done" else "Next"
                )
            }
        }
    }
}