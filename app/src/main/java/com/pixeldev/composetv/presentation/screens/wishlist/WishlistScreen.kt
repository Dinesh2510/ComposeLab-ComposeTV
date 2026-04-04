package com.pixeldev.composetv.presentation.screens.wishlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Text
import com.pixeldev.composetv.presentation.screens.home.VideoViewModel

@Composable
fun WishlistScreen( viewModel: VideoViewModel = hiltViewModel()) {

    val wishlist by viewModel.wishlist.collectAsState()

    LazyColumn {
        items(wishlist.size) {
            Text(wishlist[it].title)
        }
    }
}