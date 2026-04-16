package com.pixeldev.composetv.presentation.screens.webview

import android.graphics.Bitmap
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.presentation.components.HotstarLoader
@Composable
fun WebViewScreen(
    url: String ,
    title: String,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    val backFocusRequester = remember { FocusRequester() }
    val webViewFocusRequester = remember { FocusRequester() }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            // 🔹 TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                /*IconButton(
                    onClick = { onBack() },
                    modifier = Modifier
                        .focusRequester(backFocusRequester)
                        .focusProperties {
                            down = webViewFocusRequester
                        }
                        .focusable()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }*/

                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // 🔹 WEBVIEW
            AndroidView(
                factory = { context ->
                    WebView(context).apply {

                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true

                        // ✅ IMPORTANT FOR TV
                        isFocusable = true
                        isFocusableInTouchMode = true
                        requestFocus()

                        webViewClient = object : WebViewClient() {

                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }
                        }

                        // ✅ DPAD SCROLL SUPPORT
                        setOnKeyListener { _, keyCode, event ->
                            if (event.action == KeyEvent.ACTION_DOWN) {
                                when (keyCode) {

                                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                                        scrollBy(0, 150)
                                        true
                                    }

                                    KeyEvent.KEYCODE_DPAD_UP -> {
                                        scrollBy(0, -150)
                                        true
                                    }

                                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                        scrollBy(150, 0)
                                        true
                                    }

                                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                                        scrollBy(-150, 0)
                                        true
                                    }

                                    else -> false
                                }
                            } else false
                        }

                        loadUrl(url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(webViewFocusRequester)
                    .focusProperties {
                        up = backFocusRequester
                    }
                    .focusable()
            )
        }

        // 🔹 LOADER OVERLAY
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                HotstarLoader()
            }
        }
    }

    // ✅ AUTO FOCUS WEBVIEW ON START
    LaunchedEffect(Unit) {
        webViewFocusRequester.requestFocus()
    }
}