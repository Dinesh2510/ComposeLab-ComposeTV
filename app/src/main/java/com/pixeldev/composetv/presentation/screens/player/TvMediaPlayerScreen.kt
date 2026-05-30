package com.pixeldev.composetv.presentation.screens.player

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.pixeldev.composetv.data.local.entity.VideoEntity
import kotlinx.coroutines.delay


/* ================================================
   MAIN PLAYER SCREEN
================================================ */

@Composable
fun TvMediaPlayerScreen(
    videoItem: VideoEntity,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.Builder()
                .setUri(videoItem.videoUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(videoItem.title)
                        .setSubtitle(videoItem.description)
                        .build()
                )
                .build()
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    var isPlaying       by remember { mutableStateOf(true) }
    var isBuffering     by remember { mutableStateOf(false) }
    var showControls    by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration        by remember { mutableLongStateOf(0L) }
    var playbackError   by remember { mutableStateOf<String?>(null) }

    val playPauseFocus = remember { FocusRequester() }
    val rewindFocus    = remember { FocusRequester() }
    val forwardFocus   = remember { FocusRequester() }

    /* ---------- Player Listener ---------- */
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isBuffering = state == Player.STATE_BUFFERING
            }
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
            override fun onPlayerError(error: PlaybackException) {
                playbackError = "Playback error — please try again"
                isBuffering = false
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    /* ---------- Position Tracking ---------- */
    LaunchedEffect(exoPlayer) {
        while (true) {
            currentPosition = exoPlayer.currentPosition.coerceAtLeast(0L)
            duration        = exoPlayer.duration.coerceAtLeast(0L)
            delay(500)
        }
    }

    /* ---------- Auto Hide Controls ---------- */
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying && !isBuffering) {
            delay(5000)
            showControls = false
        }
    }

    /* ---------- FIX 2: Re-request focus whenever controls become visible ---------- */
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(100) // wait for AnimatedVisibility to finish entering
            try {
                playPauseFocus.requestFocus()
            } catch (_: Exception) { }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.Back, Key.Escape -> {
                            onBackPressed(); true
                        }
                        // FIX 2: Any key shows controls again
                        Key.DirectionCenter,
                        Key.Enter,
                        Key.DirectionUp,
                        Key.DirectionDown,
                        Key.DirectionLeft,
                        Key.DirectionRight -> {
                            if (!showControls) {
                                showControls = true
                            }
                            false // let the event pass through to focused button
                        }
                        Key.MediaPlay,
                        Key.MediaPlayPause -> {
                            if (isPlaying) exoPlayer.pause()
                            else exoPlayer.play()
                            showControls = true
                            true
                        }
                        Key.MediaFastForward -> {
                            exoPlayer.seekTo(
                                (exoPlayer.currentPosition + 10_000)
                                    .coerceAtMost(exoPlayer.duration)
                            )
                            showControls = true
                            true
                        }
                        Key.MediaRewind -> {
                            exoPlayer.seekTo(
                                (exoPlayer.currentPosition - 10_000)
                                    .coerceAtLeast(0)
                            )
                            showControls = true
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .focusable()
    ) {

        /* ---------- Video Surface ---------- */
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        /* ---------- Controls Overlay ---------- */
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            PlayerControlsOverlay(
                title           = videoItem.title,
                subtitle        = videoItem.description,
                isPlaying       = isPlaying,
                currentPosition = currentPosition,
                duration        = duration,
                playPauseFocus  = playPauseFocus,
                rewindFocus     = rewindFocus,
                forwardFocus    = forwardFocus,
                onPlayPause = {
                    if (isPlaying) exoPlayer.pause()
                    else exoPlayer.play()
                },
                onRewind = {
                    exoPlayer.seekTo(
                        (exoPlayer.currentPosition - 10_000).coerceAtLeast(0)
                    )
                },
                onForward = {
                    exoPlayer.seekTo(
                        (exoPlayer.currentPosition + 10_000)
                            .coerceAtMost(exoPlayer.duration)
                    )
                },
                onSeek = { fraction ->
                    exoPlayer.seekTo((fraction * duration).toLong())
                },
                onAnyInteraction = { showControls = true }
            )
        }

        /* ---------- FIX 1: Buffering ALWAYS on top of everything ---------- */
        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)), // dim behind spinner
                contentAlignment = Alignment.Center
            ) {
                TvBufferingIndicator(
                    size        = 72.dp,
                    strokeWidth = 5.dp,
                    color       = Color.White
                )
            }
        }

        /* ---------- Error Overlay ---------- */
        playbackError?.let { error ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TvBufferingIndicator(
                        size        = 56.dp,
                        strokeWidth = 4.dp,
                        color       = Color(0xFFFF2D78)
                    )
                    Text(
                        text       = "Playback Error",
                        color      = Color.White,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text     = error,
                        color    = Color.White.copy(alpha = 0.6f),
                        fontSize = 15.sp
                    )
                    val retryFocus = remember { FocusRequester() }
                    LaunchedEffect(Unit) {
                        delay(200)
                        retryFocus.requestFocus()
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .focusRequester(retryFocus)
                            .focusable()
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyDown &&
                                    (it.key == Key.DirectionCenter || it.key == Key.Enter)
                                ) {
                                    playbackError = null
                                    exoPlayer.prepare()
                                    exoPlayer.play()
                                    true
                                } else false
                            }
                            .padding(horizontal = 32.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text       = "↺  Retry",
                            color      = Color.White,
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/* ================================================
   CONTROLS OVERLAY
================================================ */

@Composable
fun PlayerControlsOverlay(
    title: String,
    subtitle: String,
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    playPauseFocus: FocusRequester,
    rewindFocus: FocusRequester,
    forwardFocus: FocusRequester,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onSeek: (Float) -> Unit,
    onAnyInteraction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.75f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.90f)
                    )
                )
            )
    ) {

        /* ---------- Top — Title ---------- */
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 48.dp, vertical = 32.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
        }

        /* ---------- Bottom — Controls + Seekbar ---------- */
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            /* Time Labels */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDuration(currentPosition),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatDuration(duration),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }

            /* Progress Bar */
            TvSeekBar(
                currentPosition = currentPosition,
                duration = duration,
                onSeek = onSeek
            )

            /* Buttons */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /* Rewind 10s */
                PlayerButton(
                    icon = Icons.Default.Replay10,
                    label = "-10s",
                    focusRequester = rewindFocus,
                    onClick = {
                        onRewind()
                        onAnyInteraction()
                    },
                    onFocused = onAnyInteraction
                )

                Spacer(Modifier.width(24.dp))

                /* Play / Pause — larger */
                PlayerButton(
                    icon = if (isPlaying) Icons.Default.Pause
                           else Icons.Default.PlayArrow,
                    label = if (isPlaying) "Pause" else "Play",
                    focusRequester = playPauseFocus,
                    isLarge = true,
                    onClick = {
                        onPlayPause()
                        onAnyInteraction()
                    },
                    onFocused = onAnyInteraction
                )

                Spacer(Modifier.width(24.dp))

                /* Forward 10s */
                PlayerButton(
                    icon = Icons.Default.Forward10,
                    label = "+10s",
                    focusRequester = forwardFocus,
                    onClick = {
                        onForward()
                        onAnyInteraction()
                    },
                    onFocused = onAnyInteraction
                )
            }
        }
    }
}

/* ================================================
   PLAYER BUTTON
================================================ */

@Composable
fun PlayerButton(
    icon: ImageVector,
    label: String,
    focusRequester: FocusRequester,
    isLarge: Boolean = false,
    onClick: () -> Unit,
    onFocused: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val size = if (isLarge) 72.dp else 56.dp
    val iconSize = if (isLarge) 36.dp else 26.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    if (isFocused) Color.White
                    else Color.White.copy(alpha = 0.15f)
                )
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) onFocused()
                }
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown &&
                        (keyEvent.key == Key.DirectionCenter ||
                                keyEvent.key == Key.Enter)
                    ) {
                        onClick(); true
                    } else false
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isFocused) Color.Black else Color.White,
                modifier = Modifier.size(iconSize)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = label,
            color = if (isFocused) Color.White
                    else Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            fontWeight = if (isFocused) FontWeight.Bold
                         else FontWeight.Normal
        )
    }
}

/* ================================================
   TV SEEK BAR
================================================ */

@Composable
fun TvSeekBar(
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val progress = if (duration > 0) currentPosition.toFloat() / duration else 0f
    val seekFocus = remember { FocusRequester() }

    // Animate height when focused
    val barHeight by animateDpAsState(
        targetValue = if (isFocused) 10.dp else 5.dp,
        animationSpec = tween(200),
        label = "barHeight"
    )

    // Animate color when focused
    val progressColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFFFF6B1A) else Color.White,
        animationSpec = tween(200),
        label = "progressColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp) // fixed outer height for consistent touch area
            .focusRequester(seekFocus)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.DirectionRight -> {
                            onSeek((progress + 0.02f).coerceAtMost(1f))
                            true
                        }

                        Key.DirectionLeft -> {
                            onSeek((progress - 0.02f).coerceAtLeast(0f))
                            true
                        }

                        else -> false
                    }
                } else false
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(99.dp))
        ) {
            // Track (background)
            drawRoundRect(
                color = Color.White.copy(alpha = 0.2f),
                size = this.size,
                cornerRadius = CornerRadius(99.dp.toPx())
            )

            // Buffered (slightly lighter track)
            drawRoundRect(
                color = Color.White.copy(alpha = 0.12f),
                size = this.size.copy(
                    width = this.size.width * (progress + 0.05f).coerceAtMost(1f)
                ),
                cornerRadius = CornerRadius(99.dp.toPx())
            )

            // Progress fill
            drawRoundRect(
                color = progressColor,
                size = this.size.copy(
                    width = this.size.width * progress
                ),
                cornerRadius = CornerRadius(99.dp.toPx())
            )
        }

        // Thumb dot — only when focused
        if (isFocused) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFFF6B1A),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

/* ================================================
   TIME FORMATTER
================================================ */

fun formatDuration(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSeconds = ms / 1000
    val hours   = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}