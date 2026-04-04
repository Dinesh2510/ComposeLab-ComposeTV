package com.pixeldev.composetv.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

@Composable
fun ExitDialog(
    message: String = "Are you sure wants to exit ?",
    primaryButtonText: String = "Exit",
    onPrimaryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .width(520.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF5A3A1A),
                            Color(0xFF1E1E1E)
                        ),
                        center = Offset(0f, Float.POSITIVE_INFINITY),
                        radius = 500f
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {

            // 🔹 TOP ROW (Icon + Text + Close)
            Row(
                verticalAlignment = Alignment.Top
            ) {

                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF7A00),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Exit app?",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "You'll need to relaunch to continue watching.",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 BUTTON ROW
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Primary Button
                Button(
                    onClick = onPrimaryClick,
                    colors = ButtonDefaults.colors(
                        containerColor = Color(0xFFFF7A00),
                        contentColor = Color.White
                    ),
                ) {
                    Text(primaryButtonText)
                }

                // Secondary Button (Text style)
                OutlinedButton(  onClick = onDismiss,) {
                    Text(text = "Dismiss")
                }
            }
        }
    }
}
@Composable
fun ExitDialogOverlay(
    show: Boolean,
    onDismiss: () -> Unit,
    onPrimaryClick: () -> Unit
) {
    if (!show) return

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 🔥 BLUR + DIM BACKGROUND
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(20.dp) // 🔥 blur everything behind
                .background(Color.Black.copy(alpha = 0.5f)) // dim
        )

        // 🔥 CENTER DIALOG
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            ExitDialog(
                onDismiss = onDismiss,
                onPrimaryClick = onPrimaryClick
            )
        }
    }
}
@Preview(showBackground = true, widthDp = 800, heightDp = 400, device = TV_1080p)
@Composable
fun ExitDialogPreview() {
    ExitDialog(
        onPrimaryClick = {},
        onDismiss = {}
    )
}