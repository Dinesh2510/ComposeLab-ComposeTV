package com.pixeldev.composetv.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    viewModel: VideoViewModel,
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
fun VideoCard(
    video: VideoEntity,
    viewModel: VideoViewModel, modifier: Modifier = Modifier
) {
    Card(
        onClick = {},
        scale = CardDefaults.scale(focusedScale = 1.05f),
    ) {
        Column(
            modifier = modifier.width(180.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage( // Use Coil or similar for loading images
                    model = video.card,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Play icon overlay bottom-left
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .background(Color(0x80000000), shape = CircleShape)
                )
                // Duration label bottom-right
                /*if (!video.duration.isNullOrEmpty()) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                            .background(Color(0x80000000), shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }*/
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = video.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = video.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }
    }
}
