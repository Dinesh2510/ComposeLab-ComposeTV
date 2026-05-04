package com.pixeldev.composetv

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.pixeldev.composetv.presentation.TvApp
import com.pixeldev.composetv.ui.theme.ComposeTvDemoTheme
import com.pixeldev.composetv.ui.theme.PrimeBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTvDemoTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrimeBackground) // 🔥 IMPORTANT
                )
                val config = resources.configuration
                val screenWidthDp = config.screenWidthDp
                val screenHeightDp = config.screenHeightDp
                Log.d("SCREEN", "Width: ${screenWidthDp}dp  Height: ${screenHeightDp}dp")
                val dm = resources.displayMetrics

                Log.d("SCREEN", "Width: ${config.screenWidthDp}dp Height: ${config.screenHeightDp}dp")
                Log.d("SCREEN", "Pixels: ${dm.widthPixels} x ${dm.heightPixels}")
                Log.d("SCREEN", "Density: ${dm.density}x  DPI: ${dm.densityDpi}")
               // val cardWidth = resources.getDimension(R.dimen.card_width)
               // Log.d("DIMEN", "card_width resolved to: $cardWidth px")
                TvApp()
            }
            }
        }
    }
/*// Button
            item {
                Button(
                    onClick = { showExitDialog = true },
                    modifier = Modifier.padding(start = 64.dp)
                ) {
                    Text("Open Dialog")
                }
            }
            // 🔥 DIALOG (OUTSIDE LazyColumn)
            ExitDialogOverlay(
                show = showExitDialog,
                onDismiss = { showExitDialog = false },
                onPrimaryClick = {
                    showExitDialog = false
                    // TODO: exit logic
                }
            )*/