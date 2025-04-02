package com.example.exoplayer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exoplayer.viewModel.PlayerViewModel

@Composable
fun VideoPLayerScreen(videoPath: Int, playerViewModel: PlayerViewModel = viewModel()) {

    val context = LocalContext.current
    val player = playerViewModel.playerState.collectAsState().value

    LaunchedEffect(videoPath) {
        playerViewModel.initializePlayer(context, videoPath)
    }

    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.releasePlayer()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Media3AndroidView(player)
    }
}

