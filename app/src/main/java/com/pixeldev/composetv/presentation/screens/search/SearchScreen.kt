package com.pixeldev.composetv.presentation.screens.search


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Text
import com.pixeldev.composetv.data.local.entity.VideoEntity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController

import androidx.tv.material3.Icon
import com.pixeldev.composetv.presentation.components.TopCornerGlowBackgroundCustom
import com.pixeldev.composetv.presentation.components.VideoCard
import com.pixeldev.composetv.presentation.navigation.Screen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (VideoEntity) -> Unit
) {
    TopCornerGlowBackgroundCustom(){

        TvSearchBar(viewModel,navController)
    }
}
@Composable
fun TvSearchBar(viewModel: SearchViewModel, navController: NavController) {

    val results by viewModel.results.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val randomTitles by viewModel.randomTitles.collectAsState()
    val query = viewModel.query

    val searchFocusRequester = remember { FocusRequester() }
    val listFocusRequester = remember { FocusRequester() }

    var isSearchFocused by remember { mutableStateOf(false) }
    var isFocusedBtn by remember { mutableStateOf(false) }

    //val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        //ExactSearchUI()
        // 🔍 SEARCH BOX

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                /*.background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2B0000), // deep red glow left
                            Color(0xFF120000),
                            Color(0xFF000000)
                        )
                    )
                )*/
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {


                // 🔍 Search Field
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0x33FFFFFF), // subtle glass effect
                                    Color(0x11FFFFFF)
                                )
                            )
                        )
                        .border(
                            width = if (isSearchFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(28.dp)
                        )
                        .focusRequester(searchFocusRequester)
                        .onFocusChanged {
                            isSearchFocused = it.isFocused
                            if (it.isFocused) keyboardController?.show()
                        }
                        .focusable()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        BasicTextField(
                            value = query,
                            onValueChange = { viewModel.onQueryChange(it) },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.fillMaxWidth().onPreviewKeyEvent {
                                if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                                    viewModel.onSearch()
                                    listFocusRequester.requestFocus()
                                    true
                                } else false
                            }
                        ) { innerTextField ->

                            if (query.isEmpty()) {
                                Text(
                                    "Search for an app",
                                    color = Color.Gray
                                )
                            }

                            innerTextField()
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                // 🎤 Mic Circle (LEFT SIDE - FIXED)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        //.background(Color(0xFF1E1E1E))
                        .border(
                            width = if (isFocusedBtn) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(28.dp)
                        )
                        //.focusRequester(searchFocusRequester)
                        .onFocusChanged {
                            isFocusedBtn = it.isFocused
                            if (it.isFocused) keyboardController?.show()
                        }
                        .focusable()
                    /* .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)*/,
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

            }
        }

        // 🔍 SEARCH BOX

        Spacer(modifier = Modifier.height(22.dp))

        // 🔽 Suggestions
        if (query.isNotEmpty() && suggestions.isNotEmpty() && !hasSearched) {

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                suggestions.forEach { suggestion ->

                    var isFocused by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(
                        targetValue = if (isFocused) 1.01f else 1f,
                        label = "scaleAnim"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .onFocusChanged { isFocused = it.isFocused }
                            .focusable()
                            .border(
                                width = if (isFocused) 2.dp else 0.dp,
                                color = if (isFocused) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = if (isFocused)
                                    Color.White.copy(alpha = 0.1f)
                                else
                                    Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .onPreviewKeyEvent {
                                if ((it.key == Key.Enter || it.key == Key.DirectionCenter)
                                    && it.type == KeyEventType.KeyDown
                                ) {
                                    viewModel.onQueryChange(suggestion)
                                    viewModel.onSearch()
                                    listFocusRequester.requestFocus()
                                    true
                                } else false
                            }
                            .clickable {
                                viewModel.onQueryChange(suggestion)
                                viewModel.onSearch()
                                listFocusRequester.requestFocus()
                            }
                            .padding(12.dp)
                    ) {
                        Text(suggestion, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // 🔽 Trending
        if (query.isEmpty()) {

            Column {

                Text("Try searching", color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {

                    items(
                        count = randomTitles.size,
                        key = { index -> randomTitles[index] }
                    ) { index ->

                        val title = randomTitles[index]

                        var isFocused by remember { mutableStateOf(false) }
                        val scale by animateFloatAsState(
                            targetValue = if (isFocused) 1.05f else 1f,
                            label = "chipScale"
                        )
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .border(
                                    width = if (isFocused) 2.dp else 0.dp,
                                    color = if (isFocused) Color.White else Color.Gray,
                                    shape = RoundedCornerShape(28)
                                )
                                .onPreviewKeyEvent {
                                    if ((it.key == Key.Enter || it.key == Key.DirectionCenter)
                                        && it.type == KeyEventType.KeyDown
                                    ) {
                                        viewModel.onQueryChange(title)
                                        viewModel.onSearch()
                                        listFocusRequester.requestFocus()
                                        true
                                    } else false
                                }
                                .clickable {
                                    viewModel.onQueryChange(title)
                                    viewModel.onSearch()
                                    listFocusRequester.requestFocus()
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(title, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 📺 CONTENT AREA
        when {

            !hasSearched -> {
                CenterMessage("Start typing and press OK to search")
            }

            hasSearched && results.isEmpty() -> {
                CenterMessage("No results found")
            }

            else -> {

                // 🔥 FIXED GRID (TV STYLE)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(listFocusRequester)
                ) {

                    items(results.size) { index ->

                        Box(
                            modifier = Modifier
                                .padding(10.dp) // 👈 gives room for scale (NO UI shift)
                        ) {
                            VideoCard(
                                video = results[index],
                                onFocused = {},
                                onClickCard = {
                                    navController.navigate(Screen.HomeDetails.route+ "/${results[index].title}") },
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        searchFocusRequester.requestFocus()
    }
}
@Composable
fun TvSearchBar(
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFF2A2A2A))
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(30.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) {
                    keyboardController?.show()
                }
            }
            .focusable()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            // 🔍 Search icon (always left)
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
            ) { innerTextField ->

                if (query.isEmpty()) {
                    Text(
                        "Search for an app",
                        color = Color.Gray
                    )
                }

                innerTextField()
            }

            // 🎤 Mic moves RIGHT only when focused
            if (isFocused) {
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}
@Composable
fun ExactSearchUI() {
    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var isFocusedBtn by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            /*.background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2B0000), // deep red glow left
                        Color(0xFF120000),
                        Color(0xFF000000)
                    )
                )
            )*/
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {


            // 🔍 Search Field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0x33FFFFFF), // subtle glass effect
                                Color(0x11FFFFFF)
                            )
                        )
                    )
                    .border(
                        width = if (isFocused) 2.dp else 0.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocused = it.isFocused
                        if (it.isFocused) keyboardController?.show()
                    }
                    .focusable()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) { innerTextField ->

                        if (query.isEmpty()) {
                            Text(
                                "Search for an app",
                                color = Color.Gray
                            )
                        }

                        innerTextField()
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            // 🎤 Mic Circle (LEFT SIDE - FIXED)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    //.background(Color(0xFF1E1E1E))
                    .border(
                        width = if (isFocusedBtn) 2.dp else 0.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocusedBtn = it.isFocused
                        if (it.isFocused) keyboardController?.show()
                    }
                    .focusable()
                /* .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)*/,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.White
                )
            }

        }
    }
}
@Composable
fun CenterMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 20.sp
        )
    }
}