package com.pixeldev.composetv.presentation.screens.settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.SelectableSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.pixeldev.composetv.presentation.components.TvAppBackgroundNewtt
import kotlinx.coroutines.delay

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedCategory by remember { mutableStateOf<SettingsCategory>(SettingsCategory.Profile) }

    // Auto focus first item
    val firstItemFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(200)
        try { firstItemFocus.requestFocus() } catch (_: Exception) {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A111E))
    ) {
        TvAppBackgroundNewtt(glowColor = Color(0xFFE53935))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 24.dp, end = 24.dp)
        ) {

            // ── LEFT COLUMN ──────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                )

                val categories = listOf(
                    SettingsCategory.Profile,
                    SettingsCategory.AboutUs,
                    SettingsCategory.Socials,
                    SettingsCategory.Legal
                )

                categories.forEachIndexed { index, category ->

                    val isSelected = selectedCategory == category
                    var isFocused  by remember { mutableStateOf(false) }

                    Surface(
                        selected = isSelected,
                        onClick  = { selectedCategory = category },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .then(
                                if (index == 0) Modifier.focusRequester(firstItemFocus)
                                else Modifier
                            )
                            .onFocusChanged {
                                isFocused = it.isFocused
                                if (it.isFocused) selectedCategory = category
                            },
                        shape = SelectableSurfaceDefaults.shape(
                            shape         = RoundedCornerShape(12.dp),
                            focusedShape  = RoundedCornerShape(12.dp),
                            selectedShape = RoundedCornerShape(12.dp)
                        ),
                        // ── KEY FIX: proper colors ──────────
                        colors = SelectableSurfaceDefaults.colors(

                            // normal — not selected, not focused
                            containerColor = Color.Transparent,
                            contentColor   = Color.White.copy(alpha = 0.55f),

                            // focused — D-pad lands here
                            focusedContainerColor = Color(0xFFFF6B1A), // your brand orange
                            focusedContentColor   = Color.White,

                            // selected — currently active category
                            selectedContainerColor = Color.White.copy(alpha = 0.12f),
                            selectedContentColor   = Color.White,

                            // focused + selected
                            focusedSelectedContainerColor = Color(0xFFFF6B1A),
                            focusedSelectedContentColor   = Color.White,
                        ),
                        scale = SelectableSurfaceDefaults.scale(
                            focusedScale = 1.03f
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Icon container
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isFocused || isSelected)
                                            Color.White.copy(alpha = 0.20f)
                                        else
                                            Color.White.copy(alpha = 0.07f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (isFocused) Color.White
                                    else if (isSelected) Color(0xFFFF6B1A)
                                    else Color.White.copy(alpha = 0.55f)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isFocused || isSelected)
                                    FontWeight.SemiBold
                                else FontWeight.Normal,
                                color = if (isFocused) Color.White
                                else if (isSelected) Color.White
                                else Color.White.copy(alpha = 0.55f)
                            )

                            // Active dot indicator
                            if (isSelected && !isFocused) {
                                Spacer(Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF6B1A))
                                )
                            }
                        }
                    }
                }
            }

            // ── SEPARATOR ────────────────────────────────
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically)
                    .width(1.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // ── RIGHT PANEL ───────────────────────────────
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(start = 24.dp, top = 8.dp, bottom = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .padding(28.dp)
            ) {
                Crossfade(
                    targetState = selectedCategory,
                    label = "settings_panel"
                ) { category ->
                    when (category) {
                        is SettingsCategory.Profile -> ProfileDetailView()
                        is SettingsCategory.AboutUs -> AboutUsDetailView()
                        is SettingsCategory.Socials -> SocialsDetailView()
                        is SettingsCategory.Legal   -> LegalDetailView()
                    }
                }
            }
        }
    }
}
sealed class SettingsCategory(val title: String, val icon: ImageVector) {
    object Profile : SettingsCategory("Account Profile", Icons.Default.AccountCircle)
    object AboutUs : SettingsCategory("About Us", Icons.Default.Info)
    object Socials : SettingsCategory("Developer & Socials", Icons.Default.Share)
    object Legal : SettingsCategory("Privacy & Terms", Icons.Default.Lock)
}
@Composable
fun AboutUsDetailView() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("About This App", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text(
            "Welcome to the ultimate TV streaming destination. Built entirely natively with Jetpack Compose TV for seamless performance, stunning visuals, and fluid D-pad operations.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column {
                Text("Version", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                Text("1.0.0", color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
            Column {
                Text("Build Target", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                Text("Android TV 14", color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Composable
fun ProfileDetailView() {
    var isFocused by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Account Details", style = MaterialTheme.typography.headlineSmall, color = Color.White)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(72.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Pixel Dev", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text("support@pixeldev.in", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // ❌ Never use Material3 Button in Compose TV
// ✅ Use TV Surface instead

        Surface(
            onClick  = { /* sign out logic */ },
            modifier = Modifier
                .height(48.dp)
                .onFocusChanged { isFocused = it.isFocused },
            shape  = ClickableSurfaceDefaults.shape(
                shape        = RoundedCornerShape(99.dp),
                focusedShape = RoundedCornerShape(99.dp)
            ),
            colors = ClickableSurfaceDefaults.colors(
                containerColor        = Color(0xFFE36663),         // red normal
                contentColor          = Color.White,
                focusedContainerColor = Color(0xFFFF1744),         // brighter red on focus
                focusedContentColor   = Color.White,
            ),
            scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Text(
                    "Sign Out",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SocialsDetailView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            "Connect With Us",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Visit our digital hubs for source code, tutorials, and community interactions.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.5f)
        )

        val links = listOf(
            Triple("GitHub Repository",  "View Source Code",     Icons.Default.Code),
            Triple("YouTube Channel",    "Watch Tutorials",      Icons.Default.PlayCircle),
            Triple("Official Website",   "Visit Developer Site", Icons.Default.Language)
        )

        links.forEach { (title, subtitle, icon) ->

            var isFocused by remember { mutableStateOf(false) }

            Surface(
                onClick  = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                shape  = ClickableSurfaceDefaults.shape(
                    shape        = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor        = Color.White.copy(alpha = 0.05f),
                    contentColor          = Color.White,
                    focusedContainerColor = Color(0xFFFF6B1A),  // ← brand orange
                    focusedContentColor   = Color.White,
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1.02f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isFocused) Color.White.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.07f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (isFocused) Color.White
                                else Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Column {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isFocused) Color.White.copy(alpha = 0.75f)
                                else Color.White.copy(alpha = 0.45f)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        null,
                        tint = if (isFocused) Color.White
                        else Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LegalDetailView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            "Privacy & Terms",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        val items = listOf(
            Triple("Privacy Policy",     "Read our privacy policy",     Icons.Default.Lock),
            Triple("Terms & Conditions", "Read terms and conditions",   Icons.Default.Warning)
        )

        items.forEach { (title, subtitle, icon) ->

            var isFocused by remember { mutableStateOf(false) }

            Surface(
                onClick  = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                shape  = ClickableSurfaceDefaults.shape(
                    shape        = RoundedCornerShape(12.dp),
                    focusedShape = RoundedCornerShape(12.dp)
                ),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor        = Color.White.copy(alpha = 0.05f),
                    contentColor          = Color.White,
                    focusedContainerColor = Color(0xFFFF6B1A),
                    focusedContentColor   = Color.White,
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1.02f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isFocused) Color.White.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.07f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (isFocused) Color.White
                                else Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Column {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isFocused) Color.White.copy(alpha = 0.75f)
                                else Color.White.copy(alpha = 0.45f)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        null,
                        tint = if (isFocused) Color.White
                        else Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}