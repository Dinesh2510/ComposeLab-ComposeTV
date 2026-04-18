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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {

        // 🔍 SEARCH BOX
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF2A2A2A))
                .border(
                    width = if (isSearchFocused) 2.dp else 1.dp,
                    color = if (isSearchFocused) Color.White else Color.Gray,
                    shape = RoundedCornerShape(50)
                )
                .onFocusChanged { isSearchFocused = it.isFocused }
                .focusRequester(searchFocusRequester)
                .focusable()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.LightGray,
                modifier = Modifier.clickable { viewModel.onSearch() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(modifier = Modifier.weight(1f)) {

                BasicTextField(
                    value = query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent {
                            if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                                viewModel.onSearch()
                                listFocusRequester.requestFocus()
                                true
                            } else false
                        }
                )

                if (query.isEmpty()) {
                    Text("Search", color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 🔽 Suggestions
        if (query.isNotEmpty() && suggestions.isNotEmpty() && !hasSearched) {

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                suggestions.forEach { suggestion ->

                    var isFocused by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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

                        Box(
                            modifier = Modifier
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .border(
                                    width = 2.dp,
                                    color = if (isFocused) Color.White else Color.Gray,
                                    shape = RoundedCornerShape(50)
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
                            Text(title, color = Color.White)
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