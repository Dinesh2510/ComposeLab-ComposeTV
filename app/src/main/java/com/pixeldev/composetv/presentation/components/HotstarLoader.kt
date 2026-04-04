package com.pixeldev.composetv.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun HotstarLoader(
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp
) {
    // Infinite rotation animation
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        )
    )

    // Gradient colors like JioHotstar
    val brush = Brush.sweepGradient(
        colors = JioHotstarGradient
    )

    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                rotationZ = angle
            }
            .background(Color.Transparent)
            .drawBehind {
                val diameter = size.toPx()
                val radius = diameter / 2

                drawArc(
                    brush = brush,
                    startAngle = 0f,
                    sweepAngle = 270f, // Part of circle for gradient effect
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                    topLeft = Offset.Zero,
                    size = Size(diameter, diameter)
                )
            }
    )
}
val JioHotstarGradient = listOf(
    Color(0xFF0052FF), // deep blue
    Color(0xFF3A0CA3), // indigo
    Color(0xFF7209B7), // purple
    Color(0xFFB5179E)  // pinkish purple
)