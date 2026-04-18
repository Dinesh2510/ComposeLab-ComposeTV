package com.pixeldev.composetv.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/*
 * Project: ComposeTv Demo
 * Package: com.pixeldev.composetv.presentation.components
 *
 * Copyright 2026 Dinesh
 * GitHub: https://github.com/Dinesh2510
 *
 * Created on: Saturday, April 18, 2026 at 00:32
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Composable
fun PredominantBackground() {


}
@Composable
fun TvAppBackground(
    glowColor: Color = Color(0xFF1DB954) // you will pass your HEX here
) {
    val gradient = Brush.radialGradient(
        colors = listOf(
            glowColor.copy(alpha = 0.25f),
            Color.Transparent
        ),
        center = Offset(1400f, 200f), // 🔥 top-right glow position (tweak if needed)
        radius = 1200f
    )

    val base = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0E0E0E),
            Color(0xFF121212)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(base)
            .background(gradient)
    )
}

@Composable
fun TopCornerGlowBackground(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🌑 Base background (your dark gradient)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF00050D),
                            Color(0xFF020617),
                            Color(0xFF050B18),
                            Color(0xFF0A1624)
                        )
                    )
                )
        )

        // 🔵 TOP RIGHT GLOW (like your screenshot)
        BoxWithConstraints(
            modifier = Modifier.matchParentSize()
        ) {
            val width = constraints.maxWidth.toFloat()

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x553A8DFF), // blue glow center
                                Color(0x223A8DFF),
                                Color.Transparent
                            ),
                            center = Offset(
                                x = width * 0.95f, // RIGHT
                                y = 0f             // TOP
                            ),
                            radius = width * 0.8f
                        )
                    )
            )
        }

        // ⚫ subtle vignette (optional but premium)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x66000000)
                        )
                    )
                )
        )

        // 🧩 Your Screen Content
        content()
    }
}

@Composable
fun TopCornerGlowBackgroundCustom(
    glowColor: Color = Color(0x553A8DFF),
    glowRadiusFactor: Float = 0.8f,
    glowCenterX: Float = 0.95f,
    glowCenterY: Float = 0f,
    baseColors: List<Color> = listOf(
        Color(0xFF00050D),
        Color(0xFF020617),
        Color(0xFF050B18),
        Color(0xFF0A1624)
    ),
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🌑 Base background
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(baseColors))
        )

        // 🔥 Custom Glow
        BoxWithConstraints(
            modifier = Modifier.matchParentSize()
        ) {
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                glowColor,
                                glowColor.copy(alpha = glowColor.alpha * 0.4f),
                                Color.Transparent
                            ),
                            center = Offset(
                                x = width * glowCenterX,
                                y = height * glowCenterY
                            ),
                            radius = width * glowRadiusFactor
                        )
                    )
            )
        }

        // ⚫ vignette
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0x66000000))
                    )
                )
        )

        content()
    }
}@Composable
fun TvAppBackgroundNew(
    glowColor: Color
) {
    val base = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B0B0B),
            Color(0xFF0E0E0E),
            Color(0xFF121212)
        )
    )

    // ✅ Tight TOP-RIGHT glow (main fix)
    val cornerGlow = Brush.radialGradient(
        colors = listOf(
            glowColor.copy(alpha = 0.45f),
            glowColor.copy(alpha = 0.18f),
            Color.Transparent
        ),
        center = Offset(1650f, 80f), // 🔥 push to extreme top-right
        radius = 700f                 // 🔥 reduce radius (VERY IMPORTANT)
    )

    // ✅ Subtle horizontal fade (prevents harsh edge)
    val softBlend = Brush.horizontalGradient(
        colors = listOf(
            Color.Transparent,
            glowColor.copy(alpha = 0.08f)
        ),
        startX = 900f,
        endX = 1600f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(base)
            .background(softBlend)
            .background(cornerGlow)
    )
}
@Composable
fun TvAppBackgroundNewtt(
    glowColor: Color = Color(0xFF631111) // Adjust this hex to match your specific red
) {

        val baseColor = Color(0xFF0B0B0B)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(baseColor)
                .drawBehind {
                    val width = size.width
                    val height = size.height

                    // We use a massive radius (1.5x width) to ensure
                    // the gradient looks flat and soft, not like a circle.
                    val softGlow = Brush.radialGradient(
                        0.0f to glowColor.copy(alpha = 0.5f), // Inner heat
                        0.2f to glowColor.copy(alpha = 0.2f), // Rapid softening
                        0.5f to Color.Transparent,           // Disappears halfway
                        center = Offset(width * 1.1f, -height * 0.1f), // Top-right corner
                        radius = width * 1.5f
                    )

                    drawRect(brush = softGlow)
                }
        )
}