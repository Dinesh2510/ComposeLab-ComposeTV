package com.pixeldev.composetv.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.core.ResultState
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.presentation.components.VideoCard
import com.pixeldev.composetv.presentation.screens.wishlist.WishlistScreen

@Composable
fun ExitDialog(
    message: String = "Please provide required* information to continue",
    primaryButtonText: String = "Required input",
    onPrimaryClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                // Warning Icon (orange)
                Icon(
                    imageVector = Icons.Default.Warning, // built-in warning icon
                    contentDescription = "Warning",
                    tint = Color(0xFFFF6D00), // Orange color close to screenshot
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Message Text
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Primary Button (orange background, white text)
                Button(
                    onClick = onPrimaryClick,
                    colors = ButtonDefaults.colors(
                        containerColor = Color(0xFFFF6D00),
                        contentColor = Color.White
                    ),
                    shape = ButtonDefaults.shape(RoundedCornerShape(8.dp)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(text = primaryButtonText, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Dismiss Text Button (no background, white text)
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Dismiss",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}