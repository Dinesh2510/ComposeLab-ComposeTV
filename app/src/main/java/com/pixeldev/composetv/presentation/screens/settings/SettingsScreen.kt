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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedCategory by remember { mutableStateOf<SettingsCategory>(SettingsCategory.Profile) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A111E)) // Deep premium dark background
    ) {
        TvAppBackgroundNewtt( glowColor = Color(0xFFE53935) )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 24.dp, end = 24.dp)
        ) {
            // ================= LEFT COLUMN: CATEGORIES =================
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp, start = 12.dp)
                )

                val categories = listOf(
                    SettingsCategory.Profile,
                    SettingsCategory.AboutUs,
                    SettingsCategory.Socials,
                    SettingsCategory.Legal
                )

                categories.forEach { category ->
                    var isFocused by remember { mutableStateOf(false) }
                    
                    Surface(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .onFocusChanged { 
                                isFocused = it.isFocused
                                if (it.isFocused) {
                                    selectedCategory = category // Changes content automatically on focus
                                }
                            },
                        shape = SelectableSurfaceDefaults.shape(shape = RoundedCornerShape(12.dp)),
                        colors = SelectableSurfaceDefaults.colors(
                            containerColor = Color.White.copy(alpha = 0.05f),
                            focusedContainerColor = Color.White,
                            selectedContainerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.LightGray,
                            focusedContentColor = Color.Black,
                            selectedContentColor = Color.White
                        ),
                        scale = SelectableSurfaceDefaults.scale(focusedScale = 1.04f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // Separator line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp)
            )

            // ================= RIGHT COLUMN: DYNAMIC CONTENT PANEL =================
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(start = 24.dp, top = 56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(24.dp)
            ) {
                Crossfade(targetState = selectedCategory, label = "settings_fade") { category ->
                    when (category) {
                        is SettingsCategory.Profile -> ProfileDetailView()
                        is SettingsCategory.AboutUs -> AboutUsDetailView()
                        is SettingsCategory.Socials -> SocialsDetailView()
                        is SettingsCategory.Legal -> LegalDetailView()
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
                Text("2.4.0 (Latest)", color = Color.White, style = MaterialTheme.typography.bodyMedium)
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
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Account Details", style = MaterialTheme.typography.headlineSmall, color = Color.White)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(72.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("John Doe", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text("premium_user@gmail.com", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}, colors = ButtonDefaults.colors(containerColor = Color(0xFFE53935))) {
            Text("Sign Out", color = Color.White)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SocialsDetailView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Connect With Us", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("Visit our digital hubs for source code, tutorials, and community interactions.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        val links = listOf(
            "GitHub Repository" to "View Source Code",
            "YouTube Channel" to "Watch Tutorials",
            "Official Website" to "Visit Developer Site"
        )

        links.forEach { (title, subtitle) ->
            Surface(
                onClick = { /* Handle opening URL via Intent or Nav */ },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
                colors = ClickableSurfaceDefaults.colors(containerColor = Color.White.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.LightGray)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LegalDetailView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Privacy & Terms", style = MaterialTheme.typography.headlineSmall, color = Color.White)

        Surface(
            onClick = { /* Navigate to WebViewScreen for Privacy Policy */ },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
            colors = ClickableSurfaceDefaults.colors(containerColor = Color.White.copy(alpha = 0.05f))
        ) {
            Row(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = Color.LightGray)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Privacy Policy", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }

        Surface(
            onClick = { /* Navigate to WebViewScreen for Terms */ },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
            colors = ClickableSurfaceDefaults.colors(containerColor = Color.White.copy(alpha = 0.05f))
        ) {
            Row(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = Color.LightGray)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Terms & Conditions", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
    }
}