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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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

import androidx.tv.material3.Icon
import com.pixeldev.composetv.presentation.components.TopCornerGlowBackgroundCustom
import com.pixeldev.composetv.presentation.components.VideoCard

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onItemClick: (VideoEntity) -> Unit
) {
    TopCornerGlowBackgroundCustom(){

        TvSearchBar(viewModel)
    }
}

@Composable
fun TvSearchBar(viewModel: SearchViewModel) {
    val results by viewModel.results.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()
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
                .height(60.dp)
                .border(
                    width = if (isSearchFocused) 3.dp else 1.dp,
                    color = if (isSearchFocused) Color.White else Color.Gray,
                    shape = RoundedCornerShape(30.dp)
                )
                .background(Color.DarkGray, RoundedCornerShape(30.dp))
                .onFocusChanged { isSearchFocused = it.isFocused }
                .focusRequester(searchFocusRequester)
                .focusable()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .clickable { viewModel.onSearch() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = query,
                onValueChange = { viewModel.onQueryChange(it) },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .onPreviewKeyEvent {
                        if (it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                            viewModel.onSearch()
                            listFocusRequester.requestFocus()
                            true
                        } else false
                    }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        val suggestions by viewModel.suggestions.collectAsState()

// 🔽 SUGGESTIONS when user enter words
        if (query.isNotEmpty() && suggestions.isNotEmpty()) {

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp) // 👈 controlled spacing (no weird gaps)
            ) {
                suggestions.forEach { suggestion ->

                    val focusRequester = remember { FocusRequester() }
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
                            .focusRequester(focusRequester)
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
                            .clickable {
                                viewModel.onQueryChange(suggestion)
                                viewModel.onSearch()
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = suggestion,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Trending Search
        val randomTitles by viewModel.randomTitles.collectAsState()

        if (query.isEmpty()) {

            Column {

                Text(
                    text = "Try searching",
                    color = Color.Gray,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
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
                                    width = 2.dp,
                                    color = if (isFocused) Color.White else Color.Gray,
                                    shape = RoundedCornerShape(50)
                                )
                                .background(
                                    color = if (isFocused)
                                        Color.White.copy(alpha = 0.06f)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable {
                                    viewModel.onQueryChange(title)
                                    viewModel.onSearch()
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = title,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        // 📺 CONTENT AREA
        when {

            // 💤 Idle state
            !hasSearched -> {
                CenterMessage("Start typing and press OK to search")
            }

            // 📭 Empty state
            hasSearched && results.isEmpty() -> {
                CenterMessage("No results found")
            }

            // ✅ Results
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(listFocusRequester)
                ) {
                    items(results.size ) { video ->

                        val itemFocusRequester = remember { FocusRequester() }
                        var isFocused by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusRequester(itemFocusRequester)
                                .focusable()
                                .border(
                                    width = if (isFocused) 3.dp else 0.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            VideoCard(
                                video = results[video],
                                onFocused = { },
                                onClickCard = { /*onItemClick(results[video])*/ }
                            )
                        }
                    }
                }
            }
        }
    }

    // 🎯 Auto focus search
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
fun Modifier.tvGlow(
    isFocused: Boolean,
    shape: Shape = RoundedCornerShape(50)
): Modifier {
    return if (isFocused) {
        this
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color.White,
                spotColor = Color.White
            )
            .border(
                width = 2.dp,
                color = Color.White,
                shape = shape
            )
    } else {
        this.border(
            width = 1.5.dp,
            color = Color.Gray,
            shape = shape
        )
    }
}