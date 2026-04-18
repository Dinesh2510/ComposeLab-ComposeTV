package com.pixeldev.composetv.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.pixeldev.composetv.data.local.entity.VideoEntity
import com.pixeldev.composetv.domain.model.Video
import com.pixeldev.composetv.presentation.screens.home.VideoViewModel

@Composable
fun VideoCardStd(
    video: VideoEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(180.dp)
    ) {
        CompactCard(
            modifier = Modifier.fillMaxWidth(),
            image = {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    model = video.card,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                )
            },
            title = {
                Spacer(modifier = Modifier.height(0.dp)) // hide default title
            },
            onClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = video.title,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun VideoCardStdFocus(
    video: VideoEntity,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        label = "cardScale"
    )

    val textColor by animateColorAsState(
        targetValue = if (isFocused) Color.White else Color.LightGray,
        label = "textColor"
    )

    Column(
        modifier = modifier
            .width(200.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {

        // 🔥 PASS FOCUS STATE HERE
        CompactCard(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) Color.White else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),

            image = {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    model = video.card,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                )
            },
            title = {
                Spacer(modifier = Modifier.height(0.dp))
            },
            onClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = video.title,
            color = textColor,
            fontSize = if (isFocused) 15.sp else 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoCard(
    video: VideoEntity,
    onFocused: (VideoEntity) -> Unit,
    onClickCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.width(178.dp)
    ) {

        // 🎬 CARD
        Card(
            onClick = {onClickCard() },
            colors = CardDefaults.colors(
                containerColor = Color.DarkGray,
                focusedContainerColor = Color.DarkGray
            ),
            scale = CardDefaults.scale(focusedScale = 1.1f),
            border = CardDefaults.border(
                focusedBorder = Border(
                    border = BorderStroke(3.dp, Color.White),
                    shape = RoundedCornerShape(12.dp)
                )
            ),
            shape = CardDefaults.shape(RoundedCornerShape(12.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) onFocused(video)
                }
        ) {
            AsyncImage(
                model = video.card,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🏷 TITLE (animated with focus)
        Text(
            text = video.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isFocused) Color.White else Color.White.copy(alpha = 0.7f),
            fontSize = if (isFocused) 15.sp else 13.sp,
            fontWeight = if (isFocused) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )
    }
}
